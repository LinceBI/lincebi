import searchParams from './searchParams';

export default (name, defaultValue = '') => {
	return searchParams.parse(location.search, { [name]: defaultValue })[name];
};
