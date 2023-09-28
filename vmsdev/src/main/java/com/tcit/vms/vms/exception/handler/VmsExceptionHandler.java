package com.tcit.vms.vms.exception.handler;

import com.tcit.vms.vms.dto.response.ResponseDto;
import com.tcit.vms.vms.exception.ApplicationValidationException;
import com.tcit.vms.vms.exception.UserNotFoundException;
import com.tcit.vms.vms.exception.VisitorNotFoundException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class VmsExceptionHandler {
    static final Logger logger= LogManager.getLogger(ExceptionHandler.class.getName());
    @ExceptionHandler(value = {
            VisitorNotFoundException.class,
            UserNotFoundException.class

    })
    protected ResponseEntity<ResponseDto> handle(RuntimeException runtimeException)
    {
        runtimeException.printStackTrace();
        logger.error("VmsExceptionHandler.class -->handle() with Exception # {}", runtimeException.getMessage());
        ResponseDto responseDto= new ResponseDto("",runtimeException.getMessage(), "");
        return ResponseEntity.badRequest().body(responseDto);
    }
    @ExceptionHandler(value={
            Exception.class
    })
    protected ResponseEntity<ResponseDto> handleOther(Exception e)
    {
        ResponseDto responseDto= new ResponseDto("",e.getMessage(), "");
        e.printStackTrace();
        return ResponseEntity.internalServerError().body(responseDto);

    }
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value={
            ApplicationValidationException.class
    })
    protected ResponseEntity<ResponseDto> handleOther(ApplicationValidationException e)
    {
        e.printStackTrace();
        ResponseDto responseDto= new ResponseDto("",e.getMessage(), "");
        return ResponseEntity.badRequest().body(responseDto);
    }
}


