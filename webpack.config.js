'use strict';

const autoprefixer = require('autoprefixer');
const CleanWebpackPlugin = require('clean-webpack-plugin');
const CopyWebpackPlugin = require('copy-webpack-plugin');
const cssnano = require('cssnano');
const HtmlWebpackPlugin = require('html-webpack-plugin');
const MiniCssExtractPlugin = require('mini-css-extract-plugin');
const path = require('path');
const TerserPlugin = require('terser-webpack-plugin');
const webpack = require('webpack');

const pkg = require('./package.json');

module.exports = (env, argv) => {
	const isProduction = argv.mode === 'production';
	const dist = path.join(__dirname, `dist/${isProduction ? 'prod' : 'dev'}`);

	return {
		mode: isProduction ? 'production' : 'development',
		entry: path.join(__dirname, 'src/js/app.js'),
		output: {filename: '[name].js', path: dist},
		devtool: isProduction ? false : 'eval-source-map',
		optimization: {
			minimizer: [
				new TerserPlugin({
					parallel: true,
					terserOptions: {
						output: {
							comments: false
						}
					}
				})
			]
		},
		plugins: [
			new CleanWebpackPlugin({
				cleanOnceBeforeBuildPatterns: [dist]
			}),
			new webpack.EnvironmentPlugin({
				VERSION: pkg.version
			}),
			new HtmlWebpackPlugin({
				filename: 'index.html',
				template: path.resolve(__dirname, 'src/index.html'),
				xhtml: true,
				minify: isProduction ? {
					minifyJS: true,
					minifyCSS: true,
					removeComments: true,
					collapseWhitespace: true,
					collapseInlineTagWhitespace: true,
					collapseBooleanAttributes: false,
					keepClosingSlash: true,
					maxLineLength: 512
				} : false
			}),
			new MiniCssExtractPlugin({
				filename: '[name].css',
				chunkFilename: '[id].css'
			}),
			new CopyWebpackPlugin([
			], {ignore: ['.gitkeep']})
		],
		module: {
			rules: [{
				test: /\.js$/i,
				exclude: /node_modules/,
				use: {loader: 'babel-loader'}
			}, {
				test: /\.(css|scss)$/i,
				use: [
					{loader: MiniCssExtractPlugin.loader},
					{loader: 'css-loader'},
					{loader: 'postcss-loader', options: {plugins: [
						autoprefixer(),
						...(isProduction ? [cssnano({
							preset: ['default', {
								discardComments: {
									removeAll: true
								}
							}]
						})] : [])
					]}},
					{loader: 'sass-loader', options: {data: `$env: ${argv.mode};`}}
				]
			}, {
				test: /(\.(ttf|eot|woff|woff2)|-webfont\.svg)$/i,
				use: {loader: 'file-loader', options: {name: 'fonts/[name].[ext]'}}
			}, {
				test: /\.(png|gif|jpg)$/i,
				use: {loader: 'file-loader', options: {name: 'images/[name].[ext]'}}
			}, {
				test: /\.svg$/i,
				exclude: /-webfont\.svg$/i,
				loader: ['svg-url-loader', 'svgo-loader']
			}]
		},
		devServer: {
			contentBase: dist,
			host: '0.0.0.0',
			port: 9000,
			disableHostCheck: true,
			clientLogLevel: 'info'
		}
	};
};
