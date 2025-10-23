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


if (typeof exports === 'object') {

    var joint = {
        util: require('../src/core').util,
        shapes: {},
        dia: {
            Element: require('../src/joint.dia.element').Element,
            Link: require('../src/joint.dia.link').Link
        }
    };
}

joint.shapes.org = {};

joint.shapes.org.Member = joint.dia.Element.extend({

    markup: '<g class="rotatable"><g class="scalable"><rect class="card"/><image/></g><text class="rank"/><text class="name"/></g>',

    defaults: joint.util.deepSupplement({

        type: 'org.Member',
        size: { width: 180, height: 70 },
        attrs: {

            rect: { width: 170, height: 60 },

            '.card': {
                fill: '#FFFFFF', stroke: '#000000', 'stroke-width': 2,
                'pointer-events': 'visiblePainted', rx: 10, ry: 10
            },

            image: {
		width: 48, height: 48,
                ref: '.card', 'ref-x': 10, 'ref-y': 5
            },
            
            '.rank': {
                'text-decoration': 'underline',
                ref: '.card', 'ref-x': 0.9, 'ref-y': 0.2,
                'font-family': 'Courier New', 'font-size': 14,
		'text-anchor': 'end'
            },

            '.name': {
                'font-weight': 'bold',
                ref: '.card', 'ref-x': 0.9, 'ref-y': 0.6,
                'font-family': 'Courier New', 'font-size': 14,
		'text-anchor': 'end'
            }
        }
    }, joint.dia.Element.prototype.defaults)
});

joint.shapes.org.Arrow = joint.dia.Link.extend({

    defaults: {
        type: 'org.Arrow',
        source: { selector: '.card' }, target: { selector: '.card' },
        attrs: { '.connection': { stroke: '#585858', 'stroke-width': 3 }},
        z: -1
    }
});


if (typeof exports === 'object') {

    module.exports = joint.shapes.org;
}
