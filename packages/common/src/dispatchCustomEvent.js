export default (
	type = 'custom-event',
	{
		detail = {},
		bubbles = true,
		cancelable = false,
		composed = false,
		target = window
	} = {}
) =>
	target.dispatchEvent(
		new CustomEvent(type, {
			detail,
			bubbles,
			cancelable,
			composed
		})
	);
