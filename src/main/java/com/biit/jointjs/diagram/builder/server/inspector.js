function initializeInspectorDataTypes(){
	// Common inputs.
	var CommonInspectorInputs = {
	
	    size: {
	        width: { type: 'number', min: 1, max: 500, group: 'geometry', label: 'width', index: 1 },
	        height: { type: 'number', min: 1, max: 500, group: 'geometry', label: 'height', index: 2 }
	    },
	    position: {
	        x: { type: 'number', min: 1, max: 2000, group: 'geometry', label: 'x', index: 3 },
	        y: { type: 'number', min: 1, max: 2000, group: 'geometry', label: 'y', index: 4 }
	    }
	};
	
	// Common groups.
	var CommonInspectorGroups = {
	    text: { label: 'Text', index: 1 },
	    geometry: { label: 'Geometry', index: 2 },
	};
	
	var InputDefs = {
	    text: { type: 'textarea', label: 'Text' },
	    'font-size': { type: 'range', min: 5, defaultValue: 16, max: 80, unit: 'px', label: 'Font size' },
	    'fill': { type: 'color', label: 'Fill color' },
	    'stroke': { type: 'color', defaultValue: '#000000', label: 'Stroke' },
	    'stroke-width': { type: 'range', min: 0, max: 5, step: .5, defaultValue: 0, unit: 'px', label: 'Stroke width' },
	    'stroke-dasharray': { type: 'select', options: ['0', '1', '5,5', '5,10', '10,5', '3,5', '5,1', '15,10,5,10,15'], label: 'Stroke dasharray' },
	};
	
	var BiitBaseNodeInputs = {
		attrs: {
			'.biitText': inp({
	            text: { group: 'text', index: 1 },
	            'font-size': { group: 'text', index: 2 },
	            fill: { group: 'text', index: 3 },
	            stroke: { group: 'text', index: 4 },
	            'stroke-width': { group: 'text', index: 5 }
	        })
	    }
	};
	
	//This function adds the "default" information for each field if exists.
	function inp(defs) {
	    var ret = {};
	    _.each(defs, function(def, attr) {
	
	        ret[attr] = _.extend({}, InputDefs[attr], def);
	    });
	    return ret;
	}
	
	//NOTE if label is present, then label is used to name the field, else it will use name before the ':' in this case inPorts, except because testLabel takes precedence.
	//inPorts: { type: 'list', item: { type: 'text' }, group: 'data', index: -2, label: 'testLabel!' },
	
	DiagramBuilder.InspectorDefs = {
	
	    'link': {
	
	        inputs: {
	            attrs: {
	                '.connection': {
	                    'stroke-width': { type: 'range', min: 0, max: 50, defaultValue: 1, unit: 'px', group: 'connection', label: 'stroke width', index: 1 },
	                    'stroke': { type: 'color', group: 'connection', label: 'stroke color', index: 2 },
	                    'stroke-dasharray': { type: 'select', options: ['0', '1', '5,5', '5,10', '10,5', '5,1', '15,10,5,10,15'], group: 'connection', label: 'stroke dasharray', index: 3 }
	                },
	            },
	            smooth: { type: 'toggle', group: 'connection', index: 4 },
	            manhattan: { type: 'toggle', group: 'connection', index: 5 },
	            labels: {
	                type: 'list',
	                group: 'labels',
	                attrs: {
	                    label: { 'data-tooltip': 'Set (possibly multiple) labels for the link' }
	                },
	                item: {
	                    type: 'object',
	                    properties: {
	                        position: { type: 'range', min: 0.1, max: .9, step: .1, defaultValue: .5, label: 'position', index: 2, attrs: { label: { 'data-tooltip': 'Position the label relative to the source of the link' } } },
	                        attrs: {
	                            text: {
	                                text: { type: 'text', label: 'text', defaultValue: 'label', index: 1, attrs: { label: { 'data-tooltip': 'Set text of the label' } } }
	                            }
	                        }
	                    }
	                }
	            }
	
	        },
	        groups: {
	            labels: { label: 'Labels', index: 1 },
	            'connection': { label: 'Connection', index: 2 }
	        }
	    },
	    
	    'biit.SourceNode':{
	    	inputs:  _.extend(BiitBaseNodeInputs, CommonInspectorInputs),
	    	groups: CommonInspectorGroups
	    },
	    
	    'biit.SinkNode':{
	    	inputs:  _.extend(BiitBaseNodeInputs, CommonInspectorInputs),
	    	groups: CommonInspectorGroups
	    },
	    
	    'biit.ForkNode':{
	    	inputs:  _.extend(BiitBaseNodeInputs, CommonInspectorInputs),
	    	groups: CommonInspectorGroups
	    },
	    
	    'biit.RuleNode':{
	    	inputs:  _.extend(BiitBaseNodeInputs, CommonInspectorInputs),
	    	groups: CommonInspectorGroups
	    },
	    
	    'biit.TableNode':{
	    	inputs:  _.extend(BiitBaseNodeInputs, CommonInspectorInputs),
	    	groups: CommonInspectorGroups
	    },
	    
	    'biit.CalculationNode':{
	    	inputs:  _.extend(BiitBaseNodeInputs, CommonInspectorInputs),
	    	groups: CommonInspectorGroups
	    },
	    
	    'biit.BaseRepeatNode':{
	    	inputs:  _.extend(BiitBaseNodeInputs, CommonInspectorInputs),
	    	groups: CommonInspectorGroups
	    }
	};
	
	//Note. This notation will generate automagically a list with two items for input and one for output!.
	//inPorts: { type: 'list', item: { type: 'text' }, group: 'data', index: -2, label: 'testLabel1!' },
	//outPorts: { type: 'list', item: { type: 'text' }, group: 'data', index: -1 }
}