package com.github.maheshyaddanapudi.quartz.scheduler.APICallsScheduler.service;

import com.github.maheshyaddanapudi.quartz.scheduler.APICallsScheduler.constants.Constants;
import com.github.maheshyaddanapudi.quartz.scheduler.APICallsScheduler.db.entity.AcqsExecHist;
import com.github.maheshyaddanapudi.quartz.scheduler.APICallsScheduler.db.entity.AcqsSchedMap;
import com.github.maheshyaddanapudi.quartz.scheduler.APICallsScheduler.db.repository.AcqsExecHistRepository;
import com.github.maheshyaddanapudi.quartz.scheduler.APICallsScheduler.db.repository.AcqsSchedMapRepository;
import com.github.maheshyaddanapudi.quartz.scheduler.APICallsScheduler.dto.response.embedded.oauth2.BaseResponseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class APICallQuartzExecutionHistoryService {

    @Autowired
    private AcqsExecHistRepository acqsExecHistRepository;

    @Autowired
    private AcqsSchedMapRepository acqsSchedMapRepository;

    public void deleteRecordsOlderThan(int days){

        GregorianCalendar cal = new GregorianCalendar();
        cal.setTime(new Date());
        cal.add(Calendar.DATE, -days);

        Date before = cal.getTime();

        DateFormat dateFormat = new SimpleDateFormat(Constants.DATE_FORMAT);

        this.acqsExecHistRepository.deleteRecordsOlderThan(dateFormat.format(before));
    }

    public List<AcqsExecHist> findAll(){
        return this.acqsExecHistRepository.findAll();
    }

    public List<AcqsExecHist> findAllByScheduleId(String scheduleId){
        AcqsSchedMap acqsSchedMap = this.acqsSchedMapRepository.findByQrtzSchedId(scheduleId);

        if(null!= acqsSchedMap)
        {
            return this.acqsExecHistRepository.findByAcqsSchedMap(acqsSchedMap);
        }
        else
            return new ArrayList<AcqsExecHist>();
    }

    public AcqsExecHist findByExecutionId(String executionId){
        return this.acqsExecHistRepository.findByQrtzExecId(executionId);
    }

    public BaseResponseDTO deleteByExecutionId(String executionId){

        AcqsExecHist acqsExecHist = this.acqsExecHistRepository.findByQrtzExecId(executionId);

        if(null!=acqsExecHist && acqsExecHist.getHistoryId() > 0)
        {
            if(acqsExecHist.getQuartzExecutionStatus().equalsIgnoreCase("INITIALIZED"))
            {
                return new BaseResponseDTO(false, executionId+" is still Running !!!");
            }
            else{
                this.acqsExecHistRepository.delete(acqsExecHist);
                return new BaseResponseDTO(true, executionId);
            }
        }
        else
            return new BaseResponseDTO(false, executionId+" Not found !!!");
    }

    public BaseResponseDTO deleteAllByScheduleId(String scheduleId){
        AcqsSchedMap acqsSchedMap = this.acqsSchedMapRepository.findByQrtzSchedId(scheduleId);

        if(null!= acqsSchedMap)
        {
            this.acqsExecHistRepository.deleteAll(this.acqsExecHistRepository.findByAcqsSchedMap(acqsSchedMap));
            return this.acqsExecHistRepository.findByAcqsSchedMap(acqsSchedMap).isEmpty() ? new BaseResponseDTO(true, scheduleId) : new BaseResponseDTO(false, "Unable to delete Schedule Executions.");
        }
    else
        return new BaseResponseDTO(false, scheduleId+" Not Found !!!");
    }
}
