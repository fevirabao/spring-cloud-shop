package quick.pager.shop.activity.service;

import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import quick.pager.common.constants.RedisKeys;
import quick.pager.common.dto.DTO;
import quick.pager.common.response.Response;
import quick.pager.common.service.IService;
import quick.pager.shop.activity.dto.BannerDTO;
import quick.pager.shop.activity.mapper.BannerMapper;
import quick.pager.shop.activity.redis.RedisService;
import quick.pager.shop.model.activity.Banner;

@Service
@Slf4j
public class BannerService implements IService<List<Banner>> {

    @Autowired
    private BannerMapper bannerMapper;

    @Autowired
    private RedisService redisService;

    @Override
    public Response<List<Banner>> doService(DTO dto) {

        BannerDTO bannerDTO = (BannerDTO) dto;
        String key = RedisKeys.ActivityKeys.SHOP_BANNER_LIST;
        Response<List<Banner>> response = redisService.get(key);

        if (!ObjectUtils.isEmpty(response)) {
//            return response;
        }
        response = new Response<>();
        List<Banner> banners = bannerMapper.selectAll(bannerDTO.getBannerType());
        response.setData(banners);

        redisService.set(key, response, 30 * 24 * 60 * 60L);

        return response;
    }
}
