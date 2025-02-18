//config的设置是全局的
layui.config({
 	dir: rootPath + '/js/plugin/layui/', //layui.js 所在路径（注意，如果是script单独引入layui.js，无需设定该参数。），一般情况下可以无视
 	version: version,
 	debug: false, //用于开启调试模式，默认false，如果设为true，则JS模块的节点会保留在页面
 	base: rootPath //设定扩展的Layui模块的所在目录，一般用于外部模块扩展
 }).extend({
 	common: '/js/page/common',
 	navbar: '/js/page/navbar',
 	tab: '/js/page/tab',
 	echarts: '/js/page/echarts',
 	treetable: '/js/page/treetable-lay/treetable',
 	croppers: '/js/page/cropper/croppers',
 	cropper: '/js/page/cropper/cropper',
 	yutons_sug: '/js/page/yutons-mods/yutons_sug'
 });
