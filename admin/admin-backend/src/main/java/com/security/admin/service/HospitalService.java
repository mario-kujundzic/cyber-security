package com.security.admin.service;

import com.security.admin.dto.HospitalDTO;
import com.security.admin.model.Hospital;
import com.security.admin.repository.HospitalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class HospitalService {
    @Autowired
    private HospitalRepository hospitalRepository;


    public HospitalDTO getOne(long id) {
        Hospital hospital = hospitalRepository.getOne(id);

        return new HospitalDTO(hospital);
    }

    public HospitalDTO getByCommonName(String commonName) {
        Hospital hospital = hospitalRepository.getByCommonName(commonName);

        return new HospitalDTO(hospital);
    }

    public ArrayList<HospitalDTO> getAll() {
        ArrayList<HospitalDTO> list = new ArrayList<>();

        List<Hospital> hospitals = hospitalRepository.findAll();

        for (Hospital hospital : hospitals) {
            list.add(new HospitalDTO(hospital));
        }

        return list;
    }

    public HospitalDTO create(HospitalDTO dto) {
        Hospital hospital = new Hospital(dto);

        hospital = hospitalRepository.save(hospital);
        dto = new HospitalDTO(hospital);
        return dto;
    }

    public HospitalDTO update(HospitalDTO dto) {
        Hospital hospital = new Hospital(dto);

        hospitalRepository.save(hospital);

        hospital = hospitalRepository.getOne(dto.getId());
        dto = new HospitalDTO(hospital);
        return dto;
    }

    public void delete(long id) {
        hospitalRepository.deleteById(id);
    }
}
