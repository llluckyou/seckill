package org.seckill.web;

import org.seckill.dto.Exposer;
import org.seckill.dto.SeckillExecution;
import org.seckill.dto.SeckillResult;
import org.seckill.entity.Seckill;
import org.seckill.enums.SeckillStatEnum;
import org.seckill.exception.RepeatKillException;
import org.seckill.exception.SeckillCloseException;
import org.seckill.service.SeckillService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by Administrator on 2017/5/3.
 */
@Controller
@RequestMapping("/seckill") //url: /模块/资源/{id}/细分
public class SeckillController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private SeckillService seckillService;

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public String list(Model model) {
        List<Seckill> list = seckillService.getSeckillList();
        model.addAttribute("list", list);
        System.out.println(list);
        return "list";
    }

    @RequestMapping(value = "{seckillId}/detail", method = RequestMethod.GET)
    public String detail(@PathVariable("seckillId") Long seckillId, Model model) {
        if(seckillId == null) {
            return "redirect:/seckill/list";
        }
        Seckill seckill = seckillService.getSeckillById(seckillId);
        if(seckill == null) {
            return "forward:/seckill/list";
        }else {
            model.addAttribute("seckill", seckill);
            return "detail";
        }
    }

    //ajax json
    @RequestMapping(value = "{seckillId}/exposer", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    //返回结果为json
    @ResponseBody
    public SeckillResult<Exposer> exposer(@PathVariable("seckillId") Long seckillId) {
        SeckillResult<Exposer> result;

        try {
            Exposer exposer = seckillService.exportSeckillUrl(seckillId);
            result = new SeckillResult<Exposer>(true, exposer);
        }catch (Exception e) {
            logger.error(e.getMessage());
            result = new SeckillResult<Exposer>(false, "暴露接口获取失败");
        }

        return result;
    }

    @RequestMapping(value = "{seckillId}/{md5}/execution", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public SeckillResult<SeckillExecution> execute(@PathVariable("seckillId") Long seckillId,
                                                   @PathVariable("md5") String md5,
                                                   @CookieValue(value = "killPhone", required = false) Long userPhone) {
        SeckillResult<SeckillExecution> result;
        try {
            if(userPhone != null) {
                SeckillExecution seckillExecution = seckillService.executeSeckill(seckillId, userPhone, md5);
                result = new SeckillResult<SeckillExecution>(true, seckillExecution);
            }else {
                result = new SeckillResult<SeckillExecution>(false, "手机号码为空");
            }

        }catch (SeckillCloseException e) {
            SeckillExecution seckillExecution = new SeckillExecution(seckillId, SeckillStatEnum.SUCCESS.END);
            result = new SeckillResult<SeckillExecution>(true, seckillExecution);
        }catch (RepeatKillException e) {
            SeckillExecution seckillExecution = new SeckillExecution(seckillId, SeckillStatEnum.SUCCESS.REPEAT_KILL);
            result = new SeckillResult<SeckillExecution>(true, seckillExecution);
        }
        catch (Exception e) {
            logger.error(e.getLocalizedMessage());
            result = new SeckillResult<SeckillExecution>(true, "秒杀失败");
        }
        return result;
    }

    @RequestMapping(value = "/time/now", method = RequestMethod.GET)
    @ResponseBody
    public SeckillResult<Long> time() {
        return new SeckillResult<Long>(true, System.currentTimeMillis());
    }
}
