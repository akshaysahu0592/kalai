package com.tcit.vms.vms.service;

import com.tcit.vms.vms.model.Campus;
import com.tcit.vms.vms.repository.CampusRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class CampusService {
    @Autowired
    private CampusRepository campusRepository;
    /*public ResponseDto getAllCampus() {
         List<Campus> campusList = (List<Campus>) campusRepository.findAll();
            if(campusList == null){
                return new ResponseDto("No Campus Available", "");
            }
            List<Campus> campusList1=campusList.stream().filter(Campus::isActive).collect(Collectors.toList());
            return new ResponseDto("AllCampus",campusList );
        }
*/

    public List<Campus> getCampusList() {
            List<Campus> campusList= campusRepository.findAll();
            return campusList.stream().filter(s-> Objects.nonNull(s.isActive()) && s.isActive()).collect(Collectors.toList());
        }
    public Campus createCampus(Campus campus) {
        return campusRepository.save(campus);
    }
    public Campus updateCampus(Campus campus) {
        return campusRepository.save(campus);
    }
    public Campus getCampusById(Integer id) throws RuntimeException {
        return campusRepository.findById(id).orElseThrow(() -> new RuntimeException("No Campus found for ID " + id));
    }
    public Campus deleteCampus(Integer id) {
        Campus campus = getCampusById(id);
        campus.setActive(false);
        return campusRepository.save(campus);
    }
}


