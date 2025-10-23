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
            ElementView: require('../src/joint.dia.element').ElementView,
            Link: require('../src/joint.dia.link').Link
        }
    };
    var _ = require('lodash');
}

joint.shapes.devs = {};

joint.shapes.devs.Model = joint.shapes.basic.Generic.extend(_.extend({}, joint.shapes.basic.PortsModelInterface, {

    markup: '<g class="rotatable"><g class="scalable"><rect/></g><text class="label"/><g class="inPorts"/><g class="outPorts"/></g>',
    portMarkup: '<g class="port<%= id %>"><circle/><text/></g>',

    defaults: joint.util.deepSupplement({

        type: 'devs.Model',
        size: { width: 1, height: 1 },
        
        inPorts: [],
        outPorts: [],

        attrs: {
            '.': { magnet: false },
            rect: {
                width: 150, height: 250,
                stroke: 'black'
            },
            circle: {
                r: 10,
                magnet: true,
                stroke: 'black'
            },
            text: {
                fill: 'black',
                'pointer-events': 'none'
            },
            '.label': { text: 'Model', 'ref-x': .3, 'ref-y': .2 },
            '.inPorts text': { x:-15, dy: 4, 'text-anchor': 'end' },
            '.outPorts text':{ x: 15, dy: 4 }
        }

    }, joint.shapes.basic.Generic.prototype.defaults),

    getPortAttrs: function(portName, index, total, selector, type) {

        var attrs = {};
        
        var portClass = 'port' + index;
        var portSelector = selector + '>.' + portClass;
        var portTextSelector = portSelector + '>text';
        var portCircleSelector = portSelector + '>circle';

        attrs[portTextSelector] = { text: portName };
        attrs[portCircleSelector] = { port: { id: portName || _.uniqueId(type) , type: type } };
        attrs[portSelector] = { ref: 'rect', 'ref-y': (index + 0.5) * (1 / total) };
        
        if (selector === '.outPorts') { attrs[portSelector]['ref-dx'] = 0; }

        return attrs;
    }
}));


joint.shapes.devs.Atomic = joint.shapes.devs.Model.extend({

    defaults: joint.util.deepSupplement({

        type: 'devs.Atomic',
        size: { width: 80, height: 80 },
        attrs: {
            rect: { fill: 'salmon' },
            '.label': { text: 'Atomic' },
            '.inPorts circle': { fill: 'PaleGreen' },
            '.outPorts circle': { fill: 'Tomato' }
        }

    }, joint.shapes.devs.Model.prototype.defaults)

});

joint.shapes.devs.Coupled = joint.shapes.devs.Model.extend({

    defaults: joint.util.deepSupplement({

        type: 'devs.Coupled',
        size: { width: 200, height: 300 },
        attrs: {
            rect: { fill: 'seaGreen' },
            '.label': { text: 'Coupled' },
            '.inPorts circle': { fill: 'PaleGreen' },
            '.outPorts circle': { fill: 'Tomato' }
        }

    }, joint.shapes.devs.Model.prototype.defaults)
});

joint.shapes.devs.Link = joint.dia.Link.extend({

    defaults: {
        type: 'devs.Link',
        attrs: { '.connection' : { 'stroke-width' :  2 }}
    }
});

joint.shapes.devs.ModelView = joint.dia.ElementView.extend(joint.shapes.basic.PortsViewInterface);
joint.shapes.devs.AtomicView = joint.shapes.devs.ModelView;
joint.shapes.devs.CoupledView = joint.shapes.devs.ModelView;


if (typeof exports === 'object') {

    module.exports = joint.shapes.devs;
}
