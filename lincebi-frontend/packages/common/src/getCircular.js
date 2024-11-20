export default (arr, i) => arr[((i % arr.length) + arr.length) % arr.length];
