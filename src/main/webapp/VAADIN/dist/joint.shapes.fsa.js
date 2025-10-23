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
        shapes: {
            basic: require('./joint.shapes.basic')
        },
        dia: {
            Element: require('../src/joint.dia.element').Element,
            Link: require('../src/joint.dia.link').Link
        }
    };
}

joint.shapes.fsa = {};

joint.shapes.fsa.State = joint.shapes.basic.Circle.extend({
    defaults: joint.util.deepSupplement({
        type: 'fsa.State',
        attrs: {
            circle: { 'stroke-width': 3 },
            text: { 'font-weight': 'bold' }
        }
    }, joint.shapes.basic.Circle.prototype.defaults)
});

joint.shapes.fsa.StartState = joint.dia.Element.extend({

    markup: '<g class="rotatable"><g class="scalable"><circle/></g></g>',

    defaults: joint.util.deepSupplement({

        type: 'fsa.StartState',
        size: { width: 20, height: 20 },
        attrs: {
            circle: {
                transform: 'translate(10, 10)',
                r: 10,
                fill: 'black'
            }
        }

    }, joint.dia.Element.prototype.defaults)
});

joint.shapes.fsa.EndState = joint.dia.Element.extend({

    markup: '<g class="rotatable"><g class="scalable"><circle class="outer"/><circle class="inner"/></g></g>',

    defaults: joint.util.deepSupplement({

        type: 'fsa.EndState',
        size: { width: 20, height: 20 },
        attrs: {
            '.outer': {
                transform: 'translate(10, 10)',
                r: 10,
                fill: '#FFFFFF',
                stroke: 'black'
            },

            '.inner': {
                transform: 'translate(10, 10)',
                r: 6,
                fill: '#000000'
            }
        }

    }, joint.dia.Element.prototype.defaults)
});

joint.shapes.fsa.Arrow = joint.dia.Link.extend({

    defaults: joint.util.deepSupplement({
	type: 'fsa.Arrow',
        attrs: { '.marker-target': { d: 'M 10 0 L 0 5 L 10 10 z' }},
        smooth: true
    }, joint.dia.Link.prototype.defaults)
});

if (typeof exports === 'object') {

    module.exports = joint.shapes.fsa;
}
