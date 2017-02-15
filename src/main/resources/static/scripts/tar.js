Recenseo = window.Recenseo || {}

Recenseo.Tar = {
	MAX_JUDGEMENTAL_SAMPLE_SIZE: 2000,
	MAX_SAMPLE_SIZE: 100,
	MAX_AUTOCOMPLETE_RESULT_SIZE:15,
	PAGE_SIZE:10,
	HTTP_UNAUTHORISED_ERROR_CODE:401,
    INIT_TABLE_DISPLAY_ROW_SIZE: 20,
    GLOBAL_DATE_FORMAT: 'MMM dd, yyyy hh:mm tt',
    GLOBAL_DATE_FORMAT_FOR_JAVA: 'dd/MM/yyyy',
    GLOBAL_DATE_FORMAT_FOR_JS: 'dd/MM/yyyy hh:mm tt',
	getActions: function() {
		return {
			ADD_TO_POPULATION: {
				label: 'Add Documents', 
				description:'There are no documents in the predictive coding population. Add some documents to get started',
				handler: $.proxy(this.addToPopulation, this)
			},
			CREATE_CONTROL_SAMPLE: {
				label: 'Create a Control Sample',
				description:'No control sample has been created. Once you are finishing adding documents, please create one to get population'
					+ 'and training estimates',
				handler: $.proxy(this.createControlSample, this)
			},
			UPDATE_CONTROL_SAMPLE: {
				label: 'Update Control Sample',
				description: 'The population has changed since you created your control sample. It is recommended to create a new one'
					+ ' for the best estimates',
				handler: $.proxy(this.createControlSample, this)
			},
			CREATE_SEED_SAMPLE: {
				label: 'Create Judgmental Sample',
				description:'No training samples have been created. We recommend creating a judgmental sample',
				handler: $.proxy(this.createJudgementalSample, this)
			},
			CREATE_TRAINING_SAMPLE: {
				label: 'Create a Training Sample',
				description:'There are not many training documents left to review. Finish reviewing them, or create a new sample now',
				handler: $.proxy(this.createSample, this)
			},
			EVALUATE: {
				label: 'Update Evaluation Progress',
				description: 'Please update the evaluation index to get the latest progress information',
				handler: $.proxy(this._updateEvaluationIndex, this)
			},
			PREDICT: {
				label: 'Create a Prediction',
				description: 'Training has stabilized enough that we recommend predicting the collection and generating QC samples',
				handler: $.proxy(this.createPrediction, this)
			}
		}
	},	
	init: function(opts) {
        console.log("opts:: "+JSON.stringify(opts));
		this.contextRoot = opts.contextRoot;
		this.projectId = opts.projectId;
		this.defaultAdminLocation = this.contextRoot + '/tar/setup?projectId=' + this.projectId;
    },
	_makeOptions: function(opts) {
		return $.extend({projectId: this.projectId}, opts);
	},
	_makeUrl: function(url, data) {
		return url + '?' + $.param(data, true);
	},
	syncingDocument: function() {
		this._blockUI('Syncing Documents...');
        var self = this;
		return $.post("/tar/project/sync", {projectId: this.projectId}).then(function(data) {
            if (!data.error) {
                window.location = self.defaultAdminLocation;
            } else {
                //TODO - Fix
                showMessageByForce("alert-danger", data.msg);
            }
		}).always(this._unblockUI);
	},
	createSample: function() {
		var url = this.contextRoot + '/tar/sample';
		var self = this;
		var $dlg = $('<div id="dlg-sample"/>').appendTo('body')
		.dialog({
			autoOpen: true,
			resizable: false,
			title: 'Create Sample',
			width: 240,
			height: 220,
			buttons: { 
				'Create': function() {
					var size = parseInt($('select[name=sampleSize]', $dlg).val());
					//var sampleType = $('select[name=sampleType]', $dlg).val();
					
					self._createSample(size, 'random');
					$dlg.dialog('close');
					$dlg.remove();
				},
			    'Close': function() {
			    	$dlg.dialog('close');
					$dlg.remove();
			    }
			}
		}).load(this._makeUrl(url, {projectId: this.projectId}));
	},
	_createSample: function(size, sampleType) {
		var url = this.contextRoot + '/tar/sample';
		var opts = this._makeOptions({sampleSize: size, type: sampleType});
		this._blockUI('Creating sample...');
		return $.post(url, opts).always(this._unblockUI());
	},
	getAllSavedSearches:function(workspace){
		this._blockUI('Loading saved searches...');
        return $.getJSON("/relativity/savedSearch", {projectId: this.projectId}).always(this._unblockUI);
	},
	createControlSample: function() {
		var $body = $('body', top.document);
		var url = this.contextRoot + '/tar/sample';
		var self = this;
		var $dlg = $('<div id="dlg-sample"/>').appendTo('body')
			.dialog({
				autoOpen: true,
				resizable: false,
				removeOnClose: true,
				title: 'Create Control Sample',
				width: 350,
				height: 280,		
				buttons: { 
					'Create': function() {
						var opts = self._getControlSampleOptions($dlg);
						self._blockUI('Creating sample...');
						$.post(url, opts).then(function() {
							self._unblockUI();
	                        $dlg.dialog('close');
	                        window.location = self.defaultAdminLocation;
	                    });
					},
				    'Close': function() { $dlg.dialog('close'); } 
				}
			}).load(this._makeUrl(url, {projectId: this.projectId, type: 'control'}));
	},
	_getControlSampleOptions: function($dlg) {
		var type = $('select[name=control-sample-type]', $dlg).val();
		var size = parseInt($('select[name=sampleSize]', $dlg).val());
		var confLevel = parseInt($('select[name=confidenceLevel]', $dlg).val());
		var confInterval = parseInt($('select[name=confidenceInterval]', $dlg).val());
		
		return this._makeOptions({
			type: 'control', 
			sizeType: type, 
			sampleSize: size, 
			confidenceLevel: confLevel, 
			confidenceInterval: confInterval,
            projectId:this.projectId
		});
	},
	deleteSample: function(sampleId) {
		var opts = this._makeOptions({ sampleId: sampleId });
		var url = this.contextRoot + '/tar/sample/delete'
		this._blockUI('Deleting sample...');
		return $.post(url, opts).always(this._unblockUI);
	},
	
	/* Evaluation functionality */
	_updateEvaluationIndex: function() {
		$.post(ctxRoot + '/tar/evaluate', this._makeOptions({}), function(data) {
			if (!data.error) {
				self.monitorTask(data['data'].task_id);
			} else {
                showMessageByForce("alert-danger", data.msg);
			}
		});
	},
	getYieldAtRecall: function() {
		$.get(ctxRoot + '/tar/evaluate/yar', this._makeOptions({}), function(data) {
			if (!data.error) {
				$('#yield-at-recall').yieldAtRecall({ values: data.values });
			} else {
				//TODO: Handle exception here
                showMessageByForce("alert-danger", data.msg);
			}
		});
	},
	showStabilityGauge: function() {
		var self = this;
		this.getStabilityScore().then(function(data) {
			if (!data.error) {
				self.showStabilizationGauge($('#summary-stabilization'), data['data'].stability);
			} else {
				//TODO: Handle exception here
				showMessageByForce("alert-danger", data.msg);
			}			
		});
	},
	getStabilityScore: function() {
		return $.get(ctxRoot + '/tar/evaluate/stability', this._makeOptions({}));
	},
	getYieldCurve: function($chart, opts) {
		var self = this;
		
		if ($chart.yieldCurve('instance')) {
			$chart.yieldCurve('destroy');
		}
		
		$chart.yieldCurve(opts);
		$.get(ctxRoot + '/tar/evaluate/yield', this._makeOptions({}), function(data) {
			if (!data.error) {
				$chart.yieldCurve('setData', data['data'].chart_data);
			} else {
				//TODO: Handle exception here
				showMessageByForce("alert-danger", data.msg);
			}
		});
	},
	getF1Curve: function() {
		var self = this;
		
		var chart = $('#evaluation-chart').empty();
		self.showLoadingCurve(chart, 'Training Size', 'F1', {tooltip: { formatter: self._getStandardFormatter('F-Score')} });
		$.get(ctxRoot + '/tar/evaluate/f1', this._makeOptions({}), function(data) {
			if (!data.error) {
				chart.highcharts().series[0].setData(data['data'].chart_data);
				chart.highcharts().hideLoading();
			} else {
				//TODO: Handle exception here
                showMessageByForce("alert-danger", data.msg);
			}
		});
	},
	getEnvizeScoreCurve: function() {
		var self = this;
		
		var chart = $('#evaluation-chart').empty();
		this.showLoadingCurve(chart, 'Training Size', 'Envize Score', {tooltip: { formatter: self._getStandardFormatter('Envize Score') } });
		$.get(ctxRoot + '/tar/evaluate/envizeScore', this._makeOptions({}), function(data) {
			if (!data.error) {
				chart.highcharts().series[0].setData(data['data'].chart_data);
				chart.highcharts().hideLoading();
			} else {
				//TODO: Handle exception here
                showMessageByForce("alert-danger", data.msg);
			}
		});
	},
	getEnvizeAndF1Curve: function() {
		var self = this;
		var chart = $('#evaluation-chart').empty();
		self.showLoadingCurve(chart, 'Training Size', 'Score', { legend: { verticalAlign:'top' } });
		$.get(ctxRoot + '/tar/evaluate/envizeAndF1Score', this._makeOptions({}), function(data) {
			if (!data.error) {
				chart.highcharts().addSeries({name: 'F1', data: data['data']['f1_chart_data'], color: 'rgb(124, 181, 236'});
				chart.highcharts().addSeries({name: 'Envize', data: data['data']['auprc_chart_data'], color: '#8c86f9'});
				chart.highcharts().hideLoading();
			} else {
				//TODO: Handle exception here
                showMessageByForce("alert-danger", data.msg);
			}
		});
	},
	getStabilityCurve: function() {
		var self = this;
		
		var chart = $('#evaluation-chart').empty();
		self.showLoadingCurve(chart, 'Training Size', 'Stability', {tooltip: { formatter: self._getStandardFormatter('Stability') } });
		$.get(ctxRoot + '/tar/evaluate/stabilityCurve', this._makeOptions({}), function(data) {
			if (!data.error) {
				chart.highcharts().series[0].setData(data['data'].chart_data);
				chart.highcharts().hideLoading();
			} else {
				//TODO: Handle exception here
                showMessageByForce("alert-danger", data.msg);
			}
		});
	},
	_getStandardFormatter: function(chartName) {
		return function() {
			var tt = '<span><b>' + this.y.toFixed(2) + '</b> ' + chartName + '</span><br><br>';
			tt += 'at <b>' + this.x + '</b> training documents';
			return tt;
		};
	},
	showLoadingCurve: function($el, xlabel, ylabel, extra_opts) {
		var opts = {	
			chart: {
				spaceLeft:0,
				spaceRight:0,
				spaceBottom:0,
				style: {
					fontFamily: 'Roboto Slab, serif',
					fontSize: '14px'
				}
			},
			title:null,
			credits:{enabled:false},
			tooltip: {
				valueDecimals: 2
			},
	        xAxis: {
	        	title: {
	                text: xlabel
	            },
	        },
	        yAxis: {
	            title: {
	                text: ylabel
	            },
	            min: 0.0,
	            max: 1.0
	        },
	        series: [{showInLegend: false}]
	    };
		
		if (extra_opts) {
			opts = $.extend(opts, extra_opts);
		}
		
		$el.highcharts(opts);
		$el.highcharts().showLoading();
	},
	showStabilizationGauge: function($el, val) {
		$el.stability({value: val});
	},
	    
	/* Prediction functionality */
	createPrediction: function(useControlSample) {
		var url = this.contextRoot + '/tar/prediction';
		var self = this;
		$.get(url, this._makeOptions({action:'checkControlSample'})).then(function(data) {
            if (!data.error) {
                if (data['data'].useControlSample) {
                    self._createPredictionDialog();
                } else {
                    self._quickPredict();
                }
            } else {
                //TODO - Fix
                showMessageByForce("alert-danger", data.msg);
            }

		});
	},
	_quickPredict: function() {
		var url = this.contextRoot + '/tar/prediction?quick=true';
		var self = this;
		$.post(url, this._makeOptions({}), function(data) {
            if (!data.error) {
                if (data['data'].task_id) {
                    $('#prediction-create').hide();
                    $('#prediction-task-progress').show()
                        .taskProgress({
                            taskId: [data['data'].task_id],
                            url: self.contextRoot + '/tar/task/progress',
                            alldone: function() {
                                self.reloadActiveTab();
                            }
                        });
                }
            } else {
                //TODO - Fix
                showMessageByForce("alert-danger", data.msg);
            }


		});
	},
	_createPredictionDialog: function() {
		var url = this.contextRoot + '/tar/prediction';
		
		var self = this;
		var $dlg = $('<div id="dlg-prediction"/>').appendTo('body')
		.dialog({
			autoOpen: true,
			resizable:false,
			title: 'Create Prediction',
			width: 650,
			height: 550,		
			buttons: [ 
						{ text: 'Close', id:'predict-close-btn', click: function() { $(this).dialog('close'); $dlg.remove(); } },
						{ text: 'Create', id:'predict-create-btn', disabled: true,
							click: function() {
								var opts = self._getPredictionOptions($dlg);
								self._createPrediction(opts);
								$(this).dialog('close');
								$dlg.remove();
							}
						}
					]
		}).load(this._makeUrl(url, {projectId: this.projectId}));
	},
	_getPredictionOptions: function($dlg) {
		var opts = {}
		opts.createQc = $('input[name=createQc]', $dlg).is(':checked');
		var $cl = $('select[name=confidenceLevel]', $dlg);
		opts.confidenceLevel = $cl.length > 0 ? $cl.val() : 0;
		
		var $ci = $('select[name=confidenceInterval]', $dlg);
		opts.confidenceInterval = $ci.length > 0 ? $ci.val() : 0;
		
		var yield = parseFloat($('input[name=yield]', $dlg).val());
		if (!isNaN(yield)) {
			opts.yield = yield;
		}
		
		return this._makeOptions(opts);
	},
	_createPrediction: function(opts) {
		var url = this.contextRoot + '/tar/prediction';
		var self = this;
		self._blockUI("Creating prediction...");
		$.post(url, opts).then(self._unblockUI);
	},
	getPredictionYieldCurve: function($chart, predictionId) {
		var self = this;
		
		$chart.yieldCurve();
		$.get(ctxRoot + '/tar/prediction/yield', this._makeOptions({predictionId: predictionId}), function(data) {
			if (!data.error) {
				$chart.yieldCurve('setData', data['data'].chart_data);
				$chart.yieldCurve('selectYield', data['data'].yield);
			} else {
				//TODO - Fix
                showMessageByForce("alert-danger", data.msg);
			}
		});
	},
	createQcForPredictionDialog: function(predictionId) {
		var url = this.contextRoot + '/tar/prediction/qc?predictionId=' + predictionId;
		
		var self = this;
		var $dlg = $('<div id="dlg-sample"/>').appendTo('body')
		.dialog({
			title: 'Create QC Sample(s)',
			width: 450,
			height: 300,		
			resizable:false,
			buttons: [ 
				{ text: 'Close', id:'predict-qc-close-btn', click: function() {
					$dlg.dialog('close');
					$dlg.remove();
				} },
				{ text: 'Create', id:'predict-qc-create-btn', 
					click: function() {
						var opts = self._getQcOptions($dlg, predictionId);
						self._createQc(opts);
						$dlg.dialog('close');
						$dlg.remove();
					}
				}
			]
		}).load(this._makeUrl(url, {projectId: this.projectId}));
	},
	_getQcOptions: function($dlg, predictionId) {
		var $body = $dlg.find('iframe').contents();
		var opts = {predictionId: predictionId};
		var $cl = $('select[name=confidenceLevel]', $body);
		opts.confidenceLevel = $cl.length > 0 ? $cl.val() : 0;		
		var $ci = $('select[name=confidenceInterval]', $body);
		opts.confidenceInterval = $ci.length > 0 ? $ci.val() : 0;
		
		return this._makeOptions(opts);
	},
	_createQc: function(opts) {
		var url = this.contextRoot + '/tar/prediction/qc';
		var self = this;
		$.post(url, opts);
	},
	viewCertificationReport: function(predictionId) {
		var opts = this._makeOptions({ predictionId: predictionId });
		var url = this.contextRoot + '/tar/prediction/certificationReport';
		
		var $dlg = $('<div id="dlg-sample"/>').appendTo('body')
		.dialog({
			data: opts,
			title: 'Certification Report',
			width: 400,
			height: 400,		
			resizable:false
		}).load(this._makeUrl(url, opts));
	},
	
	/*Actions/Task Status Display*/
	displayTaskProgress: function(taskIds) {
		if (taskIds.length > 0) {
			$('#summary-tasks').taskProgress({
				taskId: taskIds,
				url: this.contextRoot + '/tar/task/progress',
				complete: function(task) {
					alert('Done with task');
				},
				alldone: function() {
					$('#summary-tasks').taskProgress('destroy');
					$('#summary-task-progress').hide();
				}
			});
			$('#summary-task-progress').show();
		}
	},
	monitorTask: function(taskId) {
		
	},
	
	displayRecommendations: function(actionsArr, $cnt) {
		var self = this;
		var $cnt = $('#summary-actions');
		$('.ui-widget-content', $cnt).remove();
		
		if (actionsArr && $.isArray(actionsArr) && actionsArr.length > 0) {
			var $list = $('<ul class="ui-widget-content">')
				.css({ 'listStyle': 'none', 'textAlign': 'center'})
				.appendTo($cnt);
			
			$.each(actionsArr, function(idx, elem) {
				var $item = self._addRecommendation(elem, $list);
				if ($item) {
					$list.append($item);
					
					if (idx < (actionsArr.length - 1)) {
						$list.append('<li><hr/></li>');
					} else if (idx === 0) {
						$item.css('padding-top', 0);
					}
				}
			});

		} else {
			$('<h3 class="ui-widget-content" style="padding:24px 0; font-weight:bold">No recommendations at this time</h3>')
				.css({ textAlign: 'center' })
				.appendTo($cnt);
		}
	},
	_addRecommendation: function(action) {
		console.log("action:: "+JSON.stringify(action));
		var actionConfig = this.getActions()[action];
		if (actionConfig) {
			var $item = $('<li style="margin:12px 0;"/>');
			if (actionConfig.description) {
				$('<p style="margin-bottom:.5em"/>').text(actionConfig.description).appendTo($item);
			}
			
			var $link = $('<button class="btn btn-primary" style="width:75%">' + actionConfig.label + '</button>')
				.button()
				.appendTo($item);
			
			if (actionConfig.handler) {
				$link.on('click', $.proxy(actionConfig.handler, this));
			}
			
			return $item;
		} else {
			return undefined;
		}
	},
	
	/*Admin Dialogmethods*/
	reloadActiveTab: function() {
		var $tabs = $(".tar-tabs");
		var current_index = $tabs.tabs("option","active");
		$tabs.tabs('load',current_index);
	},

	/*Setup tab methods*/
	deleteProject: function() {
		if (!confirm("Are you sure you want to delete this project and all it's data?")) {
			return false;
		} else {
			$.ajax({
				url : this.contextRoot + '/tar/project',
				data: {projectId: this.projectId},
				method: 'DELETE'
			}).then(function() {
				alert('Project deleted successfully');
			});
		}
	},

	displayPopulationHistory: function(populationId) {
		var url = this.contextRoot + '/tar/history';
		var opts = {projectId: this.projectId};
		var $dlg = $('<div id="dlg-sample"/>').appendTo('body')
			.dialog({
				width: 550, 
				height: 'auto', 
				maxHeight: 300, 
				title: 'Population History',
				resizeable: false,
				close: function() {
					$dlg.remove();
				}
			}).load(this._makeUrl(url, opts));
	},

   	addToPopulation: function() {
        console.log("this.contextRoot:: "+JSON.stringify(this.contextRoot));
   		var url = this.contextRoot + '/tar/document';
   		var self = this;
		var $dlg = $('<div id="dlg-tools"/>').appendTo('body')
			.dialog({
				autoOpen: true,
				resizable:false,
				title: 'Add Documents',
				buttons: {
					'Add': function() {
						self._blockUI('Adding to population...');
						var savedSearchId = $('select[name=savedSearches]', $dlg).val();
						var savedSearchName = $('option[value=' + savedSearchId + ']', $dlg).text();
						var data = {action:'add', projectId:self.projectId, savedSearchId: savedSearchId, savedSearchName: savedSearchName};
	                    $.post('/tar/population', data).then(function() {
		                    $dlg.dialog('close');
							$dlg.remove();
							window.location = self.defaultAdminLocation;
	                    }).always(self._unblockUI);
					},
					'Close': function() { $dlg.dialog('close'); $dlg.remove(); }
				}
			}).load(this._makeUrl(url, {projectId: this.projectId}));
	},
	removeFromPopulation: function() {
		var url = this.contextRoot + '/tar/document';
		var self = this;
		var $dlg = $('<div id="dlg-tools"/>').appendTo('body')
			.dialog({
				autoOpen: true,
				resizable:false,
				title: 'Remove Documents',
				buttons: {
					'Remove': function() {
						self._blockUI('Removing from population...');
						var savedSearchId = $('select[name=savedSearches]', $dlg).val();
						var savedSearchName = $('option[value=' + savedSearchId + ']', $dlg).text();
						var data = {action:'remove', projectId:self.projectId, savedSearchId: savedSearchId, savedSearchName: savedSearchName};
	                    $.post('/tar/population', data).then(function() {
		                    $dlg.dialog('close');
							$dlg.remove();
							window.location = self.defaultAdminLocation;
	                    }).always(self._unblockUI);
					},
					'Close': function() { $dlg.dialog('close'); $dlg.remove(); }
				}
			}).load(this._makeUrl(url, {projectId: this.projectId}));
	},
	createJudgementalSample: function() {
		var url = this.contextRoot + '/tar/document';
		var self = this;
		var $dlg = $('<div id="dlg-tools"/>').appendTo('body')
			.dialog({
				autoOpen: true,
				resizable:false,
				title: 'Judgmental Sample',
				buttons: {
					'Create': function() {
						self._blockUI('Creating sample...');
						var savedSearchId = $('select[name=savedSearches]', $dlg).val();
						var savedSearchName = $('option[value=' + savedSearchId + ']', $dlg).text();
						var data = {type:'judgmental', projectId:self.projectId, savedSearchId: savedSearchId, savedSearchName: savedSearchName};
	                    $.post('/tar/sample', data).then(function(data) {
                            if (!data.error) {
                                $dlg.dialog('close');
                                $dlg.remove();
                                window.location = self.defaultAdminLocation;
                            } else {
                                //TODO - Fix
                                showMessageByForce("alert-danger", data.msg);
                            }
	                    }).always(self._unblockUI);
					},
					'Close': function() { $dlg.dialog('close'); $dlg.remove(); }
				}
			}).load(this._makeUrl(url, {projectId: this.projectId}));
	},
	indexDocuments: function() {
		self._blockUI('Indexing...');
		$.post().always(self._unblockUI);
	},
	_parseDocIds: function(docs) {
		return $.map(docs, function(e) { return parseInt(e); });
	},
	_blockUI: function(msg, $cnt) {
		//var $el = $cnt ? $cnt : $('body', document);
		var $el = $('body', document);
		$el.block({
			forceIframe: true,
			css:{border:'1px solid #222', padding:'8px 12px'},
			message: '<img src="' + '/images/spinner.gif" style="width:24px;height:24px;vertical-align:middle"/>' +
				'<span style="margin-left:1em;">' + (msg != undefined ? msg : 'Please wait...') + '</span>'
		});
	},
	_unblockUI: function($cnt) {
		var $el = $('body', document);
		//var $el = $cnt ? $cnt : $('body', document);
		$el.unblock();
	},
	addUser: function(userId) {
		var url = '/admin/user';
		var self = this;
		var $dlg = $('<div id="dlg-tools"/>').appendTo('body')
			.dialog({
				autoOpen: true,
				resizable:false,
                height: 570,
                width:700,
				title: '',
                buttons: [
                    {
                        id: "close",
                        text: "Close",
                        click: function () {
                            $dlg.dialog('close');
                            $dlg.remove();
                        }
                    }
                ],
                open: function (e, ui) {
                    top.$('#close').addClass('btn btn-default btn-block');
                }
			}).load(this._makeUrl(url, {userId: userId}));
	},
	addUserToProject: function(projectId) {
		console.log("SMNLOG :: projectId:"+JSON.stringify(projectId));
		var url = '/admin/addUserToProject';
		var self = this;
		var $dlg = $('<div id="dlg-tools"/>').appendTo('body')
			.dialog({
				autoOpen: true,
				resizable:false,
                height: 700,
                width:850,
				title: '',
                buttons: [
                    {
                        id: "close",
                        text: "Close",
                        click: function () {
                            $dlg.dialog('close');
                            $dlg.remove();
                        }
                    }/*,{
                        id: "update",
                        text: "Update",
                        click: function () {
                        }
                    }*/
                ],
                open: function (e, ui) {
                    top.$('#close').addClass('btn btn-default');
                    top.$('#update').addClass('btn btn-primary');
                }
			}).load(this._makeUrl(url, {projectId: projectId}));
	}
};

$.widget("icontrol.yieldCurve", {
	options: {
		allowSelection: false,
		yield: null,
		select: null,
		data: []
	},
	
	defaultChartOpts: {
		chart: {
			type: 'line',
			spaceLeft:0,
			spaceRight:0,
			spaceBottom:0,
			style: {
				fontFamily: 'Roboto Slab, serif',
				fontSize: '14px'
			}
		},
		tooltip: {
			 formatter: function () {
				 	var xVal = this.x.toFixed(2);
				 	var yVal = this.y.toFixed(2);
	                return '<b>' + yVal + '%</b> Recall after <b>' + xVal + '%</b> of documents reviewed'
	            },
	            valueDecimals: 2
		},
        xAxis: {
        	title: {
                text: '% of Review Population'
            },
            max: 100.0,
            min: 0.0
        },
        credits: {enabled:false},
        title: null,
        yAxis: {
        	max: 100.0,
        	min: 0.0,
            title: {
                text: 'Recall'
            }
        }
	},
	
	_create: function() {
		var opt = this.options;
		this.selectedPoint = undefined;
		this.chart = this._showChart();
		this.chart.highcharts().showLoading();
		
		if (opt.data.length > 0) {
			this.setData(opt.data);
		}
		
		if (opt.yield) {
			this.selectYield(opt.yield);
		}
	},
	
	setData: function(data) {
		this.chartData = this._getChartData(data);
		this.chart.highcharts().series[0].setData(this.chartData);
		this.chart.highcharts().hideLoading();
	},
	
	selectYield: function(yield) {
		var idx = this._getIndexForYield(yield);
		if (idx != null) {
			var chart = this.chart.highcharts();
			chart.series[0].data[idx].select(true);
		}
	},
	
	_getIndexForYield: function(yield) {
		var idx = null;
		var actualYield = yield * 100.0;
		for (var i=0; i < this.chartData.length - 1; i++) {
			var ptYield = this.chartData[i][0];
			var nextPtYield = this.chartData[i+1][0];
			if (ptYield <= actualYield && actualYield < nextPtYield) {
				idx = i;
				break;
			}
		}
		return idx;
	},
	
	_getChartData: function(data) {
		var chartData = [];
		for (var i=0; i < data.length; i++) {
			var point = data[i];
			var chartPoint = [point[0] * 100.0, point[1] * 100.0];
			chartData.push(chartPoint);
		}
		return chartData;
	},
	
	_showChart: function() {
		var opts = this.options;
		var $el = this.element;
		var self = this;
		
		var chartOpts = $.extend(this.defaultChartOpts, {
			series: [{
	        	showInLegend: false, 
	        	type:'spline',
	        	allowPointSelect: opts.allowSelection,
	        	marker: {
	        		enabled:true,
	        		radius:1,
	        		states: {
	        			hover: {
	        				radius:5
	        			},
	        			select: {
	        				radius: 6,
	        				fillColor:'#8c86f9',
	        				lineColor: '#B3AFF8',
	        				lineWidth: 4
	        			}
	        		}
	        	},
	        	events: {
	        		click: function(event) {
	        			var dataPoint = self.chartData[event.point.index];
	        			var pt = {'yield': dataPoint[0], 'recall': dataPoint[1]};
	        			self._select(pt);
	        			self._trigger('select', event, pt);
	        		}
	        	}
	        }]	
		});
		
		return $($el).highcharts(chartOpts);
		/*
		 , function(chart) {
			if (self.options.bias) {
				var idx = self._getIndexForBias(self.options.bias);
				var recall = self.chartData[idx].y;
				var yield = self.chartData[idx].x;
				
				var text = '<span style="font-weight:bold">Selected Recall</span> ' + recall + '<br/>';
				text += '<span style="font-weight:bold">Selected Yield</span> ' + yield + '<br/>';
                var lbl = chart.renderer.text(text, chart.plotLeft + 10, chart.plotTop + 10)
                .attr({
                    padding: 10,
                    r: 5,              
                    zIndex: 5
                }).add();
            }
		});
		*/
	},
	getSelection: function() {
		return this.selectedPoint;
	},
	_select: function(point) {
		this.selectedPoint = point;
	},
	_destroy: function() {
		this.element.empty();
	},
});

$.widget("icontrol.stability", {
	STOPS: [
	     [0.127, '#DF5252'], //.5
	     [0.22, '#D87750'], //.6
	     [0.35, '#D2994E'], //.7
	     [0.52, '#CBB94B'], //.8
	     [0.74, '#B3C549'], //.9
	     [0.87, '#8BBE46'],// .95
	     [0.985, '#65B844'], //.99
	     [1.0, '#42B242'] //.995
    ],
	options: {
		value: 0,
		inline: false
	},
	
	_create: function() {
		if (this.options.inline) {
			this._displayInline();
		} else {
			this._displayGauge();
		}
	},
	setValue: function(val) {
		if (this.options.inline) {
			this._updateValueInline(val);
		} else {
			this._updateValueGauge(val);
		}
	},
	_updateValueInline: function(val) {
		var $el = this.element;
		var color = this._getColor(val);
		$('div', $el).css('background-color', color).text(val);		
	},
	_updateValueGauge: function(val) {
		var $el = this.element;
		var $chart = $el.highcharts();
		var point = $chart.series[0].points[0];
		point.update(val);
	},
	_getColor: function(val) {		
		var normedVal = val / 100.0;
		for (var i=0; i < this.STOPS.length; i++) {
			if (normedVal <= this.STOPS[i][0]) {
				return this.STOPS[i][1];
			}
		}
	},
	_displayInline: function() {
		var $el = this.element;
		var val = this.options.value;
		
		var $circle = $('<div/>').css({
			backgroundColor: this._getColor(val),
			border:'none',
			borderRadius: '50%',
			display: 'inline-block',
			padding: '2px 4px',
			fontSize: '13px',
			color: '#fefefe',
			minWidth: '20px',
			textAlign: 'center'
		}).text(val);
		
		$el.css('display', 'inline-block')
			.append($circle)
			.append('<span style="margin-left:8px; vertical-align:text-top;">Stability</span>');
	},
	_displayGauge: function() {
		var $el = this.element;
		var val = this.options.value;
		$el.highcharts({
			chart: {
		        type: 'solidgauge'
		    },
		    pane: {
		        center: ['50%', '85%'],
		        size: '140%',
		        startAngle: -90,
		        endAngle: 90,
		        background: {
		            backgroundColor: (Highcharts.theme && Highcharts.theme.background2) || '#EEE',
		            innerRadius: '60%',
		            outerRadius: '100%',
		            shape: 'arc'
		        }
		    },
		    tooltip: {
		        enabled: false
		    },
		    title: null,
		    credits: {enabled:false},
		    plotOptions: {
		        solidgauge: {
		            dataLabels: {
		                y: -35,
		                borderWidth: 0,
		                useHTML: true
		            }
		        }
		    },
		    yAxis: {
		        stops: this.STOPS,
		        lineWidth: 0,
		        minorTickInterval: null,
		        tickPixelInterval: 400,
		        tickWidth: 0,
		        labels: {
		            enabled: false
		        },
		        min: 0,
		        max: 100,
		        title: {
		            y: -35,
		            text:'Stability'
		        }
		    },
		    
		    series: [{
		        data: [val],
		        /*dataLabels: {
		            format: '<div style="text-align:center"><span style="font-size:16px;color:' +
		                ((Highcharts.theme && Highcharts.theme.contrastTextColor) || 'black') + '">{y}</span><br/>' +
		                   '<span style="font-size:12px;color:silver">km/h</span></div>'
		        },
		        tooltip: {
		            valueSuffix: ' km/h'
		        }*/
		    }]
		});
	},
	_destroy: function() {
		this.element.empty();
	}
});

$.widget("icontrol.yieldAtRecall", {
	options: {
		recall: .75,
		values: []
	},
	
	_create: function() {
		if (this.options.values.length > 0) {
			this._createText();
			this.$slider = this._createSlider();
			this.element.show();
		}
	},
	
	_createText: function() {
		var $el = this.element;
		$('<p><span class="yar-recall"></span> recall at <span class="yar-yield"></span> of the population</p>')
			.css({ fontSize: '.9em', marginBottom:'6px' })
			.appendTo($el);
		
		$('.yar-recall, .yar-yield').css({
			fontWeight: 'bold',
			fontSize: '1.1em'
		});
	},
	
	_changeValue: function(idx) {
		var yar = this.options.values[idx];
		var recall = (yar[0] * 100).toFixed(1);
		var yield = (yar[1] * 100).toFixed(1);
		$('.yar-recall', this.element).text(recall + '%');
		$('.yar-yield', this.element).text(yield + '%');
	},
	
	_createSlider: function() {
		var $el = this.element;
		var recall = this.options.recall;

		var idx = this._getIndexValueClosestToRecall(recall);
		idx = idx == -1 ? 0 : idx;
		
		var self = this;
		var $slider = $('<div class="yar-slider"/>').appendTo($el)
			.slider({
				min: 0,
				max: this.options.values.length - 1,
				slide: function(event, ui) { self._changeValue(ui.value); },
				change: function(event, ui) { self._changeValue(ui.value); }
			});
		
		$slider.slider('option', 'value', idx);
		
		return $slider;
	},
	_getIndexValueClosestToRecall: function(recall) {
		var lastVal = -1, 
			curVal = -1;
		
		if (isNaN(recall) || recall < 0.0 || recall > 1.0) {
			return -1;
		}
		
		for (var i=0; i < this.options.values.length; i++) {
			curVal = this.options.values[i][0];
			
			if (lastVal < recall < curVal) {
				return i;
			}
			lastVal = curVal;
		}
		return -1;
	},
	_destroy: function() {
		this.element.empty();
	}
});