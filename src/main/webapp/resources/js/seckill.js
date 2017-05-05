var seckill = {
    url : {
        now: function () {
            return '/seckill/time/now';
        },
        exposer: function (seckillId) {
            return '/seckill/' + seckillId + '/exposer';
        },
        execution: function (seckillId, md5) {
            return '/seckill/' + seckillId + '/' + md5 + '/execution';
        }

    },
    handleSeckill: function (seckillId, node) {
        //处理秒杀逻辑
        node.hide().html("<button class='btn btn-primary btn-lg' id='killBtn'>秒杀开始</button>");
        $.post(seckill.url.exposer(seckillId), {}, function (result) {
            if(result && result['success']) {
                var exposer = result['data'];
                if(exposer['exposer']) {
                    //秒杀开启
                    var md5 = exposer['md5'];
                    var killUrl = seckill.url.execution(seckillId, md5);
                    console.log('killUrl:' + killUrl);
                    $('#killBtn').one('click', function () {
                        //执行秒杀操作
                        //禁用按钮
                        $(this).addClass('disable');
                        $.post(killUrl, {}, function (result) {
                            if(result && result['success']) {
                                var killResult = result['data'];
                                var state = killResult['state'];
                                var stateInfo = killResult['stateInfo'];
                                //alert(stateInfo);
                                node.html('<span class="label label-success">' + stateInfo + '</span>');
                            }
                        })
                    });
                    node.show();
                }else {
                    //秒杀未开启(由于客户机时间出现偏差,与服务器时间不一致造成)
                    var now = exposer['now'];
                    var start = exposer['start'];
                    var end = exposer['end'];

                    //再次计时，一般偏差很少
                    seckill.countdown(seckillId, now, start, end);
                }
            }else {
                console.log(result);
            }
        })
    },
    countdown: function (seckillId, nowTime, startTime, endTime) {
        var seckillBox = $('#seckillBox');

        if(endTime < nowTime) {
            seckillBox.html('秒杀结束');
        }else if(startTime > nowTime) {
            //秒杀未开始,计时
            var killTime = new Date(startTime + 1000);
            alert(killTime);
            seckillBox.countdown(killTime, function (event) {
                var format = event.strftime('秒杀倒计时: %D天  %H时 %M分 %S秒');
                seckillBox.html(format);
            }).on('finish.countdown', function () {
                //获取秒杀地址
                seckill.handleSeckill(seckillId, seckillBox);
            });
        }else {
            //秒杀开始
            seckill.handleSeckill(seckillId, seckillBox);
        }
    },

    validatePhone : function (phone) {
        if(phone && phone.length == 11 && !isNaN(phone)) {
            return true;
        }else {
            return false;
        }
    },
    detail : {
        init: function (params) {

            var killPhone = $.cookie('killPhone');

            if (!seckill.validatePhone(killPhone)) {
                var killPhoneModal = $('#killPhoneModal');
                killPhoneModal.modal({
                    show: true,
                    backdrop: 'static',
                    keyboard: false
                });
                $('#killPhoneBtn').click(function () {

                    var killPhone = $('#killPhone').val();
                    if(seckill.validatePhone(killPhone)) {
                        //alert("成功");
                        $.cookie('killPhone',killPhone, {expires: 7, path: '/seckill'} );
                        window.location.reload();
                    }else {
                        //alert("失败");
                        $('#killPhoneMessage').html('<label class="danger-label">手机号错误</label>').show(300);
                    }
                });
            }
            var seckillId = params['seckillId'];
            var startTime = params['startTime'];
            var endTime = params['endTime'];

            //通过第一个参数/seckill/time/now 返回json，里面保存了当前的时间(统一的服务器时间) result
            $.get(seckill.url.now(), {}, function (result) {
                if(result && result['success']) {
                    var nowTime = result['data'];
                    seckill.countdown(seckillId, nowTime, startTime, endTime);
                    //时间判断
                }else {
                    console.log(result);
                }
            })
        }
    }
}