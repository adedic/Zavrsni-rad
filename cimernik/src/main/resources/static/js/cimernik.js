$(document).ready(function() {
	var $loading = $('#spinner');
	var spinner = new Spinner().spin();
	$loading.append(spinner.el);
	$(document).ajaxStart(function() {
		$loading.show();
	}).ajaxStop(function() {
		$loading.hide();
	});

	$(window).on('beforeunload', function() {
		$("#spinner").show();
	});
	$(document).on('click', function() {
		$("#spinner").hide();
	});

});
