new Vue({
  name: 'share',
  el: '#app',
  data () {
    return {
      loading: true,
      link_text: '',
      link: '',
      content_img: ''
    }
  },
    created() {
        var u = navigator.userAgent;
        var isAndroid = u.indexOf('Android') > -1 || u.indexOf('Adr') > -1;  // android终端
        if (isAndroid) {
            this.content_img = '/Public/Static/Wap/AppShare/shareRecommend/img/profit_android.jpg';
            this.link_text = '共享收益规则';
            this.link = 'http://aiyunbaoapp.a56999.com/Wap/AppShare/profitRegulation2';
        } else {
            this.content_img = '/Public/Static/Wap/AppShare/shareRecommend/img/profit_ios.jpg';
            this.link_text = '共享业务收入规则';
            this.link = 'http://aiyunbaoapp.a56999.com/Wap/AppShare/profitRegulation';
        }
    },
  mounted() {
      var that = this ;
    setTimeout(function() {
        that.close_loading();
    }, 20)
  },
  methods: {
    onClick() {
        var param = {} ;
        openNativeBrowser(this.link,param);
    },
    close_loading() {
        var that = this ;
      setTimeout(function() {
          that.loading = false;
      }, 300)
    }
  }
});