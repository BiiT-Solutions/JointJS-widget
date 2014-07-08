// Namespace for our global values.
DiagramBuilder = {};

var globalCellUpdate = function ( id ){
	var cell = globalGraph.getCell(id);
	fireUpdateElementHandler(JSON.stringify(cell.toJSON()));
};

var globalGraph;

var Rappid = Backbone.Router.extend({
			routes : {
				'*path' : 'home'
			},

			initialize : function(options) {
				this.initialized = false;
				this.options = options || {};
				initializeDataTypes();
			},

			home : function() {
				if (!this.initialized) {
					this.initializeEditor();
					this.initialized = true;
				}
			},

			initializeEditor : function() {
				this.initializePaper();
				this.initializeStencil(120, 1, 'Tools');
				this.initializeSelection();
				this.initializeUpdateCallbacks();
				this.initializeHaloAndInspector();
				this.initializeClipboard();
				this.initializeCommandManager();
				// Intentionally commented out. See the `initializeValidator()`
				// method for reasons.
				// Uncomment for demo purposes.
				// this.initializeValidator();
			},

			// Create a graph, paper and wrap the paper in a PaperScroller.
			initializePaper : function() {

				this.graph = new joint.dia.Graph;
				globalGraph = this.graph;

				// Paper Scroller with autoResize enabled.
				this.paperScroller = new joint.ui.PaperScroller({
					autoResizePaper : true
				});

				this.paper = new joint.dia.Paper({
					el : this.paperScroller.el,
					width : 1000,
					height : 1000,
					gridSize : 2,
					perpendicularLinks : true,
					model : this.graph,
					validateConnection : function(cellViewS, magnetS,
							cellViewT, magnetT, end, linkView) {
						// Prevent linking from input ports.
						if (magnetS
								&& magnetS.getAttribute('type') === 'input') {
							return false;
						}
						// Prevent linking from output ports to input
						// ports within one element.
						if (cellViewS === cellViewT) {
							return false;
						}
						// Prevent linking to output ports.
						return magnetT
								&& magnetT.getAttribute('type') === 'input';
					},
					defaultLink : new joint.dia.Link({
						attrs : {
							// @TODO: scale(0) fails in Firefox
							'.marker-source' : {
								d : 'M 10 0 L 0 5 L 10 10 z',
								transform : 'scale(0.001)'
							},
							'.marker-target' : {
								d : 'M 10 0 L 0 5 L 10 10 z'
							},
							'.connection' : {
								stroke : 'black'
							// filter: { name: 'dropShadow', args: { dx:
							// 1, dy: 1, blur: 2 } }
							}
						}
					})
				});
				this.paperScroller.options.paper = this.paper;

				$('.paper-container').append(this.paperScroller.render().el);

				this.paperScroller.center();

				this.graph.on('add', this.initializeLinkTooltips, this);

				// Zoom with scroll button.
				$('.paper-scroller')
						.on(
								'mousewheel DOMMouseScroll',
								_
										.bind(
												function(evt) {
													var offset = $(
															'.paper-scroller')
															.offset();
													evt.preventDefault();
													var delta = Math
															.max(
																	-1,
																	Math
																			.min(
																					1,
																					(evt.originalEvent.wheelDelta || -evt.originalEvent.detail)));
													delta = delta / 100;
													var paperX = evt.clientX
															- offset.left;
													var paperY = evt.clientY
															- offset.top;
													this
															.zoom(
																	(this.zoomLevel || 1)
																			+ delta,
																	paperX,
																	paperY);

												}, this));

			},

			initializeLinkTooltips : function(cell) {

				if (cell instanceof joint.dia.Link) {

					var linkView = this.paper.findViewByModel(cell);
					new joint.ui.Tooltip({
						className : 'tooltip small',
						target : linkView.$('.tool-options'),
						content : 'Click to open Inspector for this link',
						left : linkView.$('.tool-options'),
						direction : 'left'
					});
				}
			},

			// Create and popoulate stencil.
			initializeStencil : function(stencilSize, stencilColumns,
					stencilCaption) {

				// Groups parameter is not passed in this project (we don't want
				// to use grouping in stencil)
				this.stencil = new joint.ui.Stencil({
					graph : this.graph,
					paper : this.paper,
					width : stencilSize
				});
				this.stencilSize = stencilSize;
				this.stencilColumns = stencilColumns;

				$("#stencil-caption").html(stencilCaption);
				$('.stencil-container').append(this.stencil.render().el);

				this.initializeStencilMembers(DiagramBuilder.shapes);

				this.initializeStencilTooltips();
			},

			initializeStencilMembers : function(memberList, group) {
				if (group === undefined) {
					this.stencil.load(memberList);
					joint.layout.GridLayout.layout(this.stencil.getGraph(), {
						columnWidth : this.stencil.options.width
								/ this.stencilColumns - 10,
						columns : this.stencilColumns,
						rowHeight : 60,
						resizeToFit : true,
						dy : 15,
						dx : 10
					});
				} else {
					this.stencil.load(Stencil.shapes.basic, name);
					joint.layout.GridLayout.layout(this.stencil.getGraph(name),
					{
						columnWidth : this.stencil.options.width
								/ this.stencilColumns - 10,
						columns : this.stencilColumns,
						rowHeight : 60,
						resizeToFit : true,
						dy : 10,
						dx : 10
					});
				}
			},

			// Create tooltips for all the shapes in stencil.
			initializeStencilTooltips : function() {
				_.each(this.stencil.graphs, function(graph) {
					graph.get('cells').each(function(cell) {
						new joint.ui.Tooltip({
							target : '.stencil [model-id="' + cell.id + '"]',
							content : cell.get('tooltip'),
							left : '.stencil',
							direction : 'left'
						});
					});
				});
			},

			// Initialization of the selection function.
			initializeSelection : function() {

				this.selection = new Backbone.Collection;
				this.selectionView = new joint.ui.SelectionView({
					paper : this.paper,
					graph : this.graph,
					model : this.selection
				});

				// Initiate selecting when the user grabs the blank area of the
				// paper while the Shift key is pressed.
				// Otherwise, initiate paper pan.
				this.paper.on('blank:pointerdown', function(evt, x, y) {
					firePickedNodeHandler(null);
					if (_.contains(KeyboardJS.activeKeys(), 'shift')) {
						this.selectionView.startSelecting(evt, x, y);
					} else {
						this.selectionView.cancelSelection();
						this.paperScroller.startPanning(evt, x, y);
					}
				}, this);

				this.paper.on('cell:pointerdown', function(cellView, evt) {
					// Select an element if CTRL/Meta key is pressed while the
					// element is clicked.
					if ((evt.ctrlKey || evt.metaKey)
							&& !(cellView.model instanceof joint.dia.Link)) {
						this.selectionView.createSelectionBox(cellView);
						this.selection.add(cellView.model);
					}
				}, this);

				this.selectionView.on('selection-box:pointerdown',
						function(evt) {
							// Unselect an element if the CTRL/Meta key is
							// pressed while a selected element is clicked.
							if (evt.ctrlKey || evt.metaKey) {
								var cell = this.selection.get($(evt.target)
										.data('model'));
								this.selectionView
										.destroySelectionBox(this.paper
												.findViewByModel(cell));
								this.selection.reset(this.selection
										.without(cell));
							}
						}, this);

				// Disable context menu inside the paper.
				// This prevents from context menu being shown when selecting
				// individual elements with Ctrl in OS X.
				this.paper.el.oncontextmenu = function(evt) {
					evt.preventDefault();
				};

				KeyboardJS.on('delete, backspace', _.bind(function(evt) {
					if (!$.contains(evt.target, this.paper.el)) {
						// remove selected elements from the paper only if the
						// target is the paper
						return;
					}

					this.commandManager.initBatchCommand();
					this.selection.invoke('remove');
					this.commandManager.storeBatchCommand();
					this.selectionView.cancelSelection();
				}, this));
			},

			initializeUpdateCallbacks : function() {
				this.graph.on('add', function(cell) {
					fireAddElementHandler(JSON.stringify(cell.toJSON()));
				});

				//Too many events.
//				this.graph.on('change:source change:target', function(link) {
//					var sourceId = link.get('source').id;
//					var targetId = link.get('target').id;
//
//					console.error("Change:! idS: " + sourceId + " idT: "
//							+ targetId);
//
//					if ((!sourceId) || (!targetId)) {
//						// link.remove();
//					}
//
//				});

				this.paper.on('cell:pointerup', function(cellView, evt, x, y) {
					if(cellView.model instanceof joint.dia.Link){
						var sourceId = cellView.model.get('source').id;
						var targetId = cellView.model.get('target').id;
						if(!sourceId || !targetId ){
							return;
						}						
					}
					fireUpdateElementHandler(JSON.stringify(cellView.model.toJSON()));
				});

				this.graph.on('remove', function(cell) {
					fireRemoveElementHandler(JSON.stringify(cell.toJSON()));
				});
			},

			initializeHaloAndInspector : function() {
				this.paper.on('cell:pointerdown',function(cellView, evt, x, y) {
					if (cellView.model instanceof joint.dia.Link || this.selection.contains(cellView.model)) {
						if (this.selection.length == 1) {
							this.selectionView
									.cancelSelection();
							this.paperScroller.startPanning(
									evt, x, y);
						}
						return;
					}
					firePickedNodeHandler(JSON.stringify(cellView.model.toJSON()));

					var halo = new joint.ui.Halo({
						graph : this.graph,
						paper : this.paper,
						cellView : cellView
					});

					halo.render();

					this.initializeHaloTooltips(halo);

					this.selectionView.cancelSelection();
					this.selection.reset([ cellView.model ]);
				},this);

				this.paper.on('cell:pointerdblclick', function(cellView, evt) {
					if (cellView.model instanceof joint.dia.Link)
						return;
					fireDoubleClickNodeHandler(JSON.stringify(cellView.model.toJSON()));
				}, this);

				this.paper.on('link:options', function(evt, cellView, x, y) {
					firePickedConnectionHandler(JSON.stringify(cellView.model.toJSON()));
				}, this);
			},

			initializeHaloTooltips : function(halo) {
				new joint.ui.Tooltip({
					className : 'tooltip small',
					target : halo.$('.remove'),
					content : 'Click to remove the object',
					direction : 'right',
					right : halo.$('.remove'),
					padding : 15
				});
				new joint.ui.Tooltip({
					className : 'tooltip small',
					target : halo.$('.fork'),
					content : 'Click and drag to clone and connect the object in one go',
					direction : 'left',
					left : halo.$('.fork'),
					padding : 15
				});
				new joint.ui.Tooltip({
					className : 'tooltip small',
					target : halo.$('.clone'),
					content : 'Click and drag to clone the object',
					direction : 'left',
					left : halo.$('.clone'),
					padding : 15
				});
				new joint.ui.Tooltip({
					className : 'tooltip small',
					target : halo.$('.unlink'),
					content : 'Click to break all connections to other objects',
					direction : 'right',
					right : halo.$('.unlink'),
					padding : 15
				});
				new joint.ui.Tooltip({
					className : 'tooltip small',
					target : halo.$('.link'),
					content : 'Click and drag to connect the object',
					direction : 'left',
					left : halo.$('.link'),
					padding : 15
				});
				new joint.ui.Tooltip({
					className : 'tooltip small',
					target : halo.$('.rotate'),
					content : 'Click and drag to rotate the object',
					direction : 'right',
					right : halo.$('.rotate'),
					padding : 15
				});
			},

			initializeClipboard : function() {

				this.clipboard = new joint.ui.Clipboard;

				KeyboardJS.on('ctrl + c', _.bind(function() {
					// Copy all selected elements and their associated links.
					this.clipboard.copyElements(this.selection, this.graph, {
						translate : {
							dx : 20,
							dy : 20
						},
						useLocalStorage : true
					});
				}, this));

				KeyboardJS.on('ctrl + v', _.bind(function() {
					this.clipboard.pasteCells(this.graph);
					this.selectionView.cancelSelection();

					this.clipboard.pasteCells(this.graph, {
						link : {
							z : -1
						},
						useLocalStorage : true
					});

					// Make sure pasted elements get selected immediately. This
					// makes the UX better as
					// the user can immediately manipulate the pasted elements.
					var selectionTmp = [];

					this.clipboard.each(function(cell) {

						if (cell.get('type') === 'link')
							return;

						// Push to the selection not to the model from the
						// clipboard but put the model into the graph.
						// Note that they are different models. There is no
						// views associated with the models
						// in clipboard.
						selectionTmp.push(this.graph.getCell(cell.id));
						this.selectionView.createSelectionBox(this.paper
								.findViewByModel(cell));
					}, this);

					this.selection.reset(selectionTmp);
				}, this));

				KeyboardJS.on('ctrl + x', _.bind(function() {

					var originalCells = this.clipboard.copyElements(
							this.selection, this.graph, {
								useLocalStorage : true
							});
					this.commandManager.initBatchCommand();
					_.invoke(originalCells, 'remove');
					this.commandManager.storeBatchCommand();
					this.selectionView.cancelSelection();
				}, this));
			},

			// Check if cell in command is a link. Continue validating if yes,
			// otherwise stop.
			isLink : function(err, command, next) {
				if (command.data.type === 'link')
					return next(err);
				// otherwise stop validating (don't call next validation function)
			},

			// check whether the link can be connected to the ceratin cell or
			// pinned to the paper
			connectivityTarget : function(err, command, next) {
				// The cell in Parameter "command" is meant to be a link.
				var targetId = command.data.next.target.id;
				// source and target are both cells
				if (targetId) {
					// All is valid
					globalCellUpdate(command.data.id);
					return next(err);
				} else {
					// One or both of them are not cells
					return next("Links can't be pinned to the paper");
				}
			},
			
			connectivitySource : function(err, command, next) {
				// The cell in Parameter "command" is meant to be a link.
				var sourceId = command.data.next.source.id;
				// source and target are both cells
				if (sourceId) {
					// All is valid
					globalCellUpdate(command.data.id);
					return next(err);
				} else {
					// One or both of them are not cells
					return next("Links can't be pinned to the paper");
				}
			},
			
			initializeCommandManager : function() {
				
				this.commandManager = new joint.dia.CommandManager({
					graph : this.graph
				});

				this.validator = new joint.dia.Validator({
					commandManager : this.commandManager
				});

				// register validation functions
				this.validator.validate('change:target', this.isLink, this.connectivityTarget);
				this.validator.validate('change:source', this.isLink, this.connectivitySource);

				KeyboardJS.on('ctrl + z', _.bind(function() {

					this.commandManager.undo();
					this.selectionView.cancelSelection();
				}, this));

				KeyboardJS.on('ctrl + y', _.bind(function() {

					this.commandManager.redo();
					this.selectionView.cancelSelection();
				}, this));
			},

			initializeValidator : function() {

				// This is just for demo purposes. Every application has its own
				// validation rules or no validation
				// rules at all.

				this.validator = new joint.dia.Validator({
					commandManager : this.commandManager
				});

				this.validator.validate('change:position change:size add', _
						.bind(function(err, command, next) {

							if (command.action === 'add' && command.batch)
								return next();

							var cell = command.data.attributes || this.graph.getCell(command.data.id).toJSON();
							var area = g.rect(cell.position.x, cell.position.y, cell.size.width, cell.size.height);

							if (_.find(this.graph.getElements(), function(e) {
								var position = e.get('position');
								var size = e.get('size');
								return (e.id !== cell.id && area.intersect(g
										.rect(position.x, position.y,
												size.width, size.height)));

							}))
								return next("Another cell in the way!");
						}, this));

				this.validator.on('invalid', function(message) {

					$('.statusbar-container').text(message).addClass('error');

					_.delay(
							function() {

								$('.statusbar-container').text('').removeClass(
										'error');

							}, 1500);
				});
			},

			openAsPNG : function() {

				var windowFeatures = 'menubar=yes,location=yes,resizable=yes,scrollbars=yes,status=yes';
				var windowName = _.uniqueId('png_output');
				var imageWindow = window.open('', windowName, windowFeatures);

				this.paper.toPNG(function(dataURL) {
					imageWindow.document.write('<img src="' + dataURL + '"/>');
				}, {
					padding : 10
				});
			},

			zoom : function(newZoomLevel, ox, oy) {

				if (_.isUndefined(this.zoomLevel)) {
					this.zoomLevel = 1;
				}

				if (newZoomLevel > 0.2 && newZoomLevel < 20) {

					if (ox === undefined) {
						ox = (this.paper.el.scrollLeft + this.paper.el.clientWidth / 2)
								/ this.zoomLevel;
						oy = ox;
					} else {
						ox = this.paper.el.scrollLeft + (ox / this.zoomLevel);
						oy = this.paper.el.scrollTop + (oy / this.zoomLevel);
					}

					this.paper.scale(newZoomLevel, newZoomLevel, ox, oy);

					this.zoomLevel = newZoomLevel;
				}
			},

			zoomOut : function() {
				this.zoom((this.zoomLevel || 1) - 0.2);
			},
			zoomIn : function() {
				this.zoom((this.zoomLevel || 1) + 0.2);
			},

			updateCellAttrs : function(cellData) {
				this.graph.getCell(cellData.id);
				// We are not using this because it breaks all the links.
				// this.graph.getCell(cellData.id).set('attrs', cellData.attrs);
				// Instead we use this function. This function combines current
				// attribures with newer ones, so we must be sure to pass every
				// attribute with the desired value.
				this.graph.getCell(cellData.id).attr(cellData.attrs);
			},

			updateLinkText : function(cellData) {
				var link = this.graph.getCell(cellData.id);
				link.label(0, cellData.labels[0]);
			}
		});
