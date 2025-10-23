/*-
 * #%L
 * JointJs Diagram Builder for Vaadin
 * %%
 * Copyright (C) 2022 - 2025 BiiT Sourcing Solutions S.L.
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * #L%
 */
/*! Rappid - the diagramming toolkit

Copyright (c) 2013 client IO

 2014-05-01 


This Source Code Form is subject to the terms of the Rappid License
, v. 2.0. If a copy of the Rappid License was not distributed with this
file, You can obtain one at http://jointjs.com/license/rappid_v2.txt
 or from the Rappid archive as was distributed by client IO. See the LICENSE file.*/


// SelectionView
// =============

// `SelectionView` implements selecting group of elements and moving the selected elements in one go.
// Typically, the selection will be bound to the `Shift` key
// and selecting/deselecting individual elements to the `Ctrl` key.

// Example usage:

// var graph = new joint.dia.Graph;
// var paper = new joint.dia.Paper({ model: graph });
// var selection = new Backbone.Collection;
// var selectionView = new joint.ui.SelectionView({ paper: paper, graph: graph, model: selection });

// // Bulk selecting group of elements by creating a rectangular selection area.
// paper.on('blank:pointerdown', selectionView.startSelecting);

// // Selecting individual elements with click and the `Ctrl`/`Command` key.
// paper.on('cell:pointerup', function(cellView, evt) {
//      if ((evt.ctrlKey || evt.metaKey) && !(cellView.model instanceof joint.dia.Link)) {
//              selectionView.createSelectionBox(cellView);
//              selection.add(cellView.model);
//      }
// });

// // Deselecting previously selected elements with click and the `Ctrl`/`Command` key.
// selectionView.on('selection-box:pointerdown', function(evt) {
//      if (evt.ctrlKey || evt.metaKey) {
//              var cell = selection.get($(evt.target).data('model'));
//              selectionView.destroySelectionBox(paper.findViewByModel(cell));
//              selection.reset(selection.without(cell));
//      }
// });

joint.ui.SelectionView = Backbone.View.extend({

    options: {

        paper: undefined,
        graph: undefined
    },

    className: 'selection',

    events: {

        'mousedown .selection-box': 'startTranslatingSelection',
        'touchstart .selection-box': 'startTranslatingSelection'
    },
    
    initialize: function() {

        _.bindAll(this, 'startSelecting', 'stopSelecting', 'adjustSelection');

        $(document.body).on('mousemove.selectionView touchmove.selectionView', this.adjustSelection);

        this.listenTo(this.options.graph, 'reset', this.cancelSelection);
        this.listenTo(this.options.paper, 'scale', this.updateSelectionBoxes);
        this.listenTo(this.options.graph, 'remove change', function(cell, opt) {
            // Do not react on changes that happened inside the selectionView.
            if (!opt['selectionView_' + this.cid]) this.updateSelectionBoxes();
        });

        this.options.paper.$el.append(this.$el);

        // A counter of existing boxes. We don't want to update selection boxes on
        // each graph change when no selection boxes exist.
        this._boxCount = 0;
    },

    startTranslatingSelection: function(evt) {

        evt.stopPropagation();

        evt = joint.util.normalizeEvent(evt);

        this._action = 'translating';

	this.options.graph.trigger('batch:start');
        
        var snappedClientCoords = this.options.paper.snapToGrid(g.point(evt.clientX, evt.clientY));
        this._snappedClientX = snappedClientCoords.x;
        this._snappedClientY = snappedClientCoords.y;

        this.trigger('selection-box:pointerdown', evt);

        this.mouseupRegister();
    },

    startSelecting: function(evt) {

        evt = joint.util.normalizeEvent(evt);

        this.cancelSelection();
        
        this._action = 'selecting';

        this._clientX = evt.clientX;
        this._clientY = evt.clientY;
        
        // Normalize `evt.offsetX`/`evt.offsetY` for browsers that don't support it (Firefox).
        var paperElement = evt.target.parentElement || evt.target.parentNode;
        var paperOffset = $(paperElement).offset();
        var paperScrollLeft = paperElement.scrollLeft;
        var paperScrollTop = paperElement.scrollTop;

        this._offsetX = evt.offsetX === undefined ? evt.clientX - paperOffset.left + window.pageXOffset + paperScrollLeft : evt.offsetX;
        this._offsetY = evt.offsetY === undefined ? evt.clientY - paperOffset.top + window.pageYOffset + paperScrollTop : evt.offsetY;

        this.$el.css({

            width: 1,
            height: 1,
            left: this._offsetX,
            top: this._offsetY
            
        }).show();

        this.mouseupRegister();
    },

    adjustSelection: function(evt) {

        evt = joint.util.normalizeEvent(evt);

        var dx;
        var dy;
        
        switch (this._action) {

          case 'selecting':

            dx = evt.clientX - this._clientX;
            dy = evt.clientY - this._clientY;

            var width = this.$el.width();
            var height = this.$el.height();
            var left = parseInt(this.$el.css('left'), 10);
            var top = parseInt(this.$el.css('top'), 10);

            this.$el.css({

                left: dx < 0 ? this._offsetX + dx : left,
                top: dy < 0 ? this._offsetY + dy : top,
                width: Math.abs(dx),
                height: Math.abs(dy)
            });
            break;

          case 'translating':

            var snappedClientCoords = this.options.paper.snapToGrid(g.point(evt.clientX, evt.clientY));
            var snappedClientX = snappedClientCoords.x;
            var snappedClientY = snappedClientCoords.y;
            
            dx = snappedClientX - this._snappedClientX;
            dy = snappedClientY - this._snappedClientY;

            // This hash of flags makes sure we're not adjusting vertices of one link twice.
            // This could happen as one link can be an inbound link of one element in the selection
            // and outbound link of another at the same time.
            var processedLinks = {};
            
            this.model.each(function(element) {

                // TODO: snap to grid.

                // Make sure that selectionView won't update itself when not necessary
                var opt = {};
                opt['selectionView_' + this.cid] = true;

                // Translate the element itself.
                element.translate(dx, dy, opt);

                // Translate link vertices as well.
                var connectedLinks = this.options.graph.getConnectedLinks(element);

                _.each(connectedLinks, function(link) {

                    if (processedLinks[link.id]) return;

                    var vertices = link.get('vertices');
                    if (vertices && vertices.length) {

                        var newVertices = [];
                        _.each(vertices, function(vertex) {

                            newVertices.push({ x: vertex.x + dx, y: vertex.y + dy });
                        });

                        link.set('vertices', newVertices, opt);
                    }
                    
                    processedLinks[link.id] = true;
                });
                
            }, this);

            if (dx || dy) {

		var paperScale = V(this.options.paper.viewport).scale();
		dx *= paperScale.sx;
		dy *= paperScale.sy;

		// Translate also each of the `selection-box`.
		this.$('.selection-box').each(function() {

                    var left = parseFloat($(this).css('left'), 10);
                    var top = parseFloat($(this).css('top'), 10);
                    $(this).css({ left: left + dx, top: top + dy });
		});

		this._snappedClientX = snappedClientX;
		this._snappedClientY = snappedClientY;
	    }

            this.trigger('selection-box:pointermove', evt);
            break;
        }
    },

    stopSelecting: function(evt) {

        switch (this._action) {

          case 'selecting':

            var offset = this.$el.offset();
            var width = this.$el.width();
            var height = this.$el.height();

            // Convert offset coordinates to the local point of the <svg> root element viewport.
            var localPoint = V(this.options.paper.viewport).toLocalPoint(offset.left, offset.top);

            // Take page scroll into consideration.
            localPoint.x -= window.pageXOffset;
            localPoint.y -= window.pageYOffset;

            // Convert width and height to take current viewport scale into account
	    var paperScale = V(this.options.paper.viewport).scale();
            width /= paperScale.sx;
            height /= paperScale.sy;

            var elementViews = this.options.paper.findViewsInArea(g.rect(localPoint.x, localPoint.y, width, height));

            if (elementViews.length) {

                // Create a `selection-box` `<div>` for each element covering its bounding box area.
                _.each(elementViews, this.createSelectionBox, this);

                // The root element of the selection switches `position` to `static` when `selected`. This
                // is neccessary in order for the `selection-box` coordinates to be relative to the
                // `paper` element, not the `selection` `<div>`.
                this.$el.addClass('selected');
                
            } else {

                // Hide the selection box if there was no element found in the area covered by the
                // selection box.
                this.$el.hide();
            }
            
            this.model.reset(_.pluck(elementViews, 'model'));
            break;

          case 'translating':

	    this.options.graph.trigger('batch:stop');
            this.trigger('selection-box:pointerup', evt);
            // Everything else is done during the translation.
            break;

        default:
            // Hide selection if the user clicked somehwere else in the document.
            this.cancelSelection();
            break;
        }

        delete this._action;
    },

    cancelSelection: function() {

        this.$el.hide().empty().removeClass('selected');
        this.model.reset([]);
        this._boxCount = 0;
    },

    destroySelectionBox: function(elementView) {

        this.$('[data-model="' + elementView.model.get('id') + '"]').remove();
        if (this.$('.selection-box').length === 0) {

            this.$el.hide().removeClass('selected');
        }

        this._boxCount = Math.max(0, this._boxCount - 1);
    },

    createSelectionBox: function(elementView) {
        
        var viewBbox = elementView.getBBox();

        var $selectionBox = $('<div/>', { 'class': 'selection-box', 'data-model': elementView.model.get('id') });
        $selectionBox.css({ left: viewBbox.x, top: viewBbox.y, width: viewBbox.width, height: viewBbox.height });
        this.$el.append($selectionBox);

        this.$el.addClass('selected').show();

        this._boxCount++;
    },

    updateSelectionBoxes: function() {

        if (!this._boxCount) return;

        this.$el.hide().removeClass('selected')
            .find('.selection-box').each(_.bind(function(index, element) {

                var removedId = $(element).remove().attr('data-model');

                // try to find an element with the same id in the selection collection and
                // find the view for this model.
                var view = this.options.paper.findViewByModel(this.model.get(removedId));

                if (view) {
                    // The view doesn't need to exist on the paper anymore as we use this method
                    // as a handler for element removal.
                    this.createSelectionBox(view);
                }

            }, this));
    },

    mouseupRegister: function() {

        var eventType = typeof window.ontouchend === 'function'
            ? 'touchend.selectionView'
            : 'mouseup.selectionView';

        $(document.body).one(eventType, this.stopSelecting);
    },

    remove: function() {

        Backbone.View.prototype.remove.apply(this, arguments);

        $(document.body).off('.selectionView');
    }

});
