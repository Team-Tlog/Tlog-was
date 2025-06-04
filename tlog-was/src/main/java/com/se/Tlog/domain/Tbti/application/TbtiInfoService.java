package com.se.Tlog.domain.Tbti.application;

import com.se.Tlog.domain.ApplicationService;
import com.se.Tlog.domain.Tbti.controller.dto.CreateTbtiInfoReq;
import com.se.Tlog.domain.Tbti.controller.dto.TbtiInfoRes;
import com.se.Tlog.domain.Tbti.domain.Tbti;
import com.se.Tlog.domain.Tbti.domain.TbtiInfo;
import com.se.Tlog.domain.Tbti.repository.jpa.TbtiInfoRepository;
import com.se.Tlog.global.exception.CustomException;
import com.se.Tlog.global.response.error.ErrorType;

import lombok.RequiredArgsConstructor;

@ApplicationService
@RequiredArgsConstructor
public class TbtiInfoService {
    private final TbtiInfoRepository tbtiInfoRepository;
    
    public void createTbtiInfo(CreateTbtiInfoReq request) {
        if (tbtiInfoRepository.existsByTbtiString(request.tbtiString()))
            throw new CustomException(ErrorType.ALREADY_EXISTS_TBTI_INFO);
        
        tbtiInfoRepository.save(
                TbtiInfo.create(
                        new Tbti(request.tbtiString()), 
                        request.imageUrl(), 
                        request.secondName(), 
                        request.description()));
    }
    
    public TbtiInfoRes getTbtiInfo(String tbtiString) {
        Tbti tbti = new Tbti(tbtiString);
        TbtiInfo tbtiInfo = tbtiInfoRepository.findByTbtiString(tbti.toString());
        if (tbtiInfo == null)
            return TbtiInfoRes.getNullDto(tbti);
        else
            return new TbtiInfoRes(
                    tbti.toString(),
                    tbtiInfo.getImageUrl(),
                    tbtiInfo.getSecondName(),
                    tbtiInfo.getDescription());
    }
    
    public void deleteTbtiInfo(String tbtiString) {
        Tbti tbti = new Tbti(tbtiString);
        TbtiInfo tbtiInfo = tbtiInfoRepository.findByTbtiString(tbti.toString());
        if (tbtiInfo == null)
            return;
        tbtiInfoRepository.delete(tbtiInfo);
    }
}
