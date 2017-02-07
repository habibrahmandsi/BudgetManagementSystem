(function($) {

$.widget("icontrol.taskProgress", {
	options: { 
		taskId : undefined,
		url : undefined,
		pollingInterval: 1000,
		
		//Event methods
		progress: undefined,
		complete: undefined,
		alldone: undefined
	},
	
	_create: function() {
		var o = this.options;
		var el = this.element;
		var self = this;
		if (o.taskId && o.url) {
			this._progressBars = [];
			if ($.isArray(o.taskId)) {
				$.each(o.taskId, function(idx, id) {
					var pb = self._createProgressBar(id); 
					if (idx < (o.taskId.length - 1)) {
						pb.css('margin-bottom', '12px');
					}
					
					self._progressBars.push(pb);
					el.append(pb);
				});
			} else {
				var pb = this._createProgressBar(o.taskId);
				this._progressBars.push(pb);
				el.append(pb);
			}
			
			this._trigger('start');
			this._updateProgress();			
		}
	},
	_createProgressBar: function(taskId) {
		var container = $('<div id="task-id-' + taskId + '"/>');
		var progress = $('<div class="task-progress-bar"/>')
		.css('position', 'relative')
		.progressbar();
		
		$('<span/>').css({
			position:'absolute',
			left:'42%',
			top:'.5em',
			'font-weight': 'bold'
		}).appendTo(progress);
		
		var status = $('<div class="task-progress-status" style="margin-bottom:3px;"/>')
		.append('<span></span>');
		
		container.append(status);	
		container.append(progress);
		
		return container;
	},
	
	destroy: function() {
		$.each(this._progressBars, function(idx, $el) {
			$el.children('.task-progress-bar').progressbar('destroy');
		});
		
		this.element.empty();
	},
	
	_updateProgress: function() {
		var o = this.options;
		var self = this;
		
		var taskIds = $.isArray(o.taskId) ? o.taskId : [o.taskId];
		$.ajaxSettings.traditional = true;
		$.get(o.url, {
			action: 'progress',
			taskId : taskIds
		}, function(taskList) {
			$.each(taskList, function(idx, task) {
				self._updateTask(task);
			});
			
			if (self._isRunningTasks(taskList)) {
				setTimeout(function() { self._updateProgress(); }, o.pollingInterval);
			} else {
				self._trigger('alldone', undefined, taskList);	
			}
		});
	},
	_isRunningTasks: function(taskList) {
		var self = this;
		var notFinishedList = $.grep(taskList, function(task) { return !self._isFinished(task)} );
		return notFinishedList.length > 0;
	},	
	_updateTask: function(task) {
		var $cnt = $('#task-id-' + task.id);
		var $status = $cnt.children('.task-progress-status');
		var $progress = $cnt.children('.task-progress-bar');
		
		if (!this._isFinished(task)) {
			$status.text(task.statusMessage);			
			var progress = parseFloat(task.percentComplete * 100).toFixed(1);	
			$progress.progressbar("value", parseInt(progress));
			$('span', $progress).text((isNaN(progress) ? '' : progress + '%'));
			this._trigger('progress', undefined, task);			
		} else {
			$progress.progressbar("value", 100);
			this._trigger('complete', undefined, task);
			$cnt.remove();
		}
	},	
	_isFinished: function(task) {
		return task.status != 'RUNNING' && task.status != 'QUEUED';
	}
});

})(jQuery);