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


joint.format.gexf = {};

joint.format.gexf.toCellsArray = function(xmlString, makeElement, makeLink) {

    // Parse the `xmlString` into a DOM tree.
    var parser = new DOMParser();
    var dom = parser.parseFromString(xmlString, 'text/xml');
    if (dom.documentElement.nodeName == "parsererror") {
        throw new Error('Error while parsing GEXF file.');
    }

    // Get all nodes and edges.
    var nodes = dom.documentElement.querySelectorAll('node');
    var edges = dom.documentElement.querySelectorAll('edge');

    // Return value.
    var cells = [];

    _.each(nodes, function(node) {

        var size = parseFloat(node.querySelector('size').getAttribute('value'));
        
        var element = makeElement({
            id: node.getAttribute('id'),
            width: size,
            height: size,
            label: node.getAttribute('label')
        });
        
        cells.push(element);
    });

    _.each(edges, function(edge) {

        var link = makeLink({ source: edge.getAttribute('source'), target: edge.getAttribute('target') });
        cells.unshift(link);
    });

    return cells;
};
