import fetch from 'unfetch';
import getContextPath from './getContextPath';

let workspaceDirPromise = null;

const getWorkspaceDir = async () => {
	const contextPath = await getContextPath();
	const endpoint = `${contextPath}api/session/userWorkspaceDir`;
	const response = await fetch(endpoint, {
		method: 'GET',
		headers: { 'Content-Type': 'text/plain' },
	});
	if (response.status === 200) {
		return response.text();
	} else {
		return false;
	}
};

export default async (...args) => {
	if (workspaceDirPromise === null) {
		workspaceDirPromise = getWorkspaceDir(...args);
	}
	return workspaceDirPromise;
};
