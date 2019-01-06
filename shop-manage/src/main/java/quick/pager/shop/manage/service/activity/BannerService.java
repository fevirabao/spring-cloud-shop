package quick.pager.shop.manage.service.activity;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import java.util.Date;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import quick.pager.common.constants.Constants;
import quick.pager.common.dto.DTO;
import quick.pager.common.response.Response;
import quick.pager.common.service.IService;
import quick.pager.shop.manage.dto.BannerDTO;
import quick.pager.shop.manage.mapper.BannerMapper;
import quick.pager.shop.model.activity.Banner;

/**
 * banner 服务
 */
@Service
@Slf4j
public class BannerService implements IService {

    @Autowired
    private BannerMapper bannerMapper;

    @Override
    public Response doService(DTO dto) {

        BannerDTO bannerDTO = (BannerDTO) dto;
        Response response = new Response();

        switch (bannerDTO.getEvent()) {
            case Constants.Event.ADD:
            case Constants.Event.MODIFY:
                modifyBanner(bannerDTO);
                break;
            case Constants.Event.LIST:
                response = queryBanners(bannerDTO);
                break;
        }

        return response;
    }

    /**
     * 查询列表
     */
    private Response queryBanners(BannerDTO bannerDTO) {
        Response<List<Banner>> response = new Response<>();

        PageHelper.startPage(bannerDTO.getPage(), bannerDTO.getPageSize());
        List<Banner> banners = bannerMapper.selectBanner(bannerDTO.getTitle(),bannerDTO.getBannerType());

        PageInfo<Banner> pageInfo = new PageInfo<>(banners);

        response.setTotal(pageInfo.getTotal());
        response.setData(pageInfo.getList());

        return response;
    }


    /**
     * 修改配置
     */
    private void modifyBanner(BannerDTO dto) {
        Banner banner = new Banner();
        BeanUtils.copyProperties(dto, banner);
        banner.setShareChannel(JSON.toJSONString(dto.getShareChannel()));

        if (Constants.Event.ADD.equals(dto.getEvent())) {
            banner.setCreateTime(new Date());
            bannerMapper.insertSelective(banner);
        } else {
            bannerMapper.updateByPrimaryKeySelective(banner);
        }
    }
}
