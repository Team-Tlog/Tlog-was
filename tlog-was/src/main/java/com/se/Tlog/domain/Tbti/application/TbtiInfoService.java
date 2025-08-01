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
                        request.secondName(), 
                        request.description(),
                        new Tbti(request.preferredtbtiString()),
                        new Tbti(request.notPreferredtbtiString())));
    }
    
    public TbtiInfoRes getTbtiInfo(Tbti tbti) {
        TbtiInfo tbtiInfo = tbtiInfoRepository.findByTbtiString(tbti.toString());
        if (tbtiInfo == null)
            return TbtiInfoRes.getNullDto(tbti);
        else
            return new TbtiInfoRes(
                    tbti.toString(),
                    tbtiInfo.getSecondName(),
                    tbtiInfo.getDescription(),
                    tbtiInfo.getPreferred(),
                    tbtiInfo.getNotPreferred());
    }
    
    public TbtiInfoRes getTbtiInfo(String tbtiString) {
        return getTbtiInfo(new Tbti(tbtiString));
    }
    
    public void deleteTbtiInfo(String tbtiString) {
        Tbti tbti = new Tbti(tbtiString);
        TbtiInfo tbtiInfo = tbtiInfoRepository.findByTbtiString(tbti.toString());
        if (tbtiInfo == null)
            return;
        tbtiInfoRepository.delete(tbtiInfo);
    }
}
