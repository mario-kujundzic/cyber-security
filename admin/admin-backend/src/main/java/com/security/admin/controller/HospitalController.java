package com.security.admin.controller;

import com.security.admin.dto.GenericMessageDTO;
import com.security.admin.dto.HospitalDTO;
import com.security.admin.service.HospitalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(value = "/api/hospitals", produces = MediaType.APPLICATION_JSON_VALUE)
public class HospitalController {

    @Autowired
    private HospitalService hospitalService;


    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('READ_PRIVILEGE')")
    public ResponseEntity<HospitalDTO> getOneHospital(@PathVariable long id) {
        HospitalDTO hospital = hospitalService.getOne(id);

        return new ResponseEntity<HospitalDTO>(hospital, HttpStatus.OK);
    }

    @GetMapping
    @PreAuthorize("hasAuthority('READ_PRIVILEGE')")
    public ResponseEntity<List<HospitalDTO>> getAllHospitals() {
        ArrayList<HospitalDTO> hospitalDTOs = hospitalService.getAll();

        return new ResponseEntity<>(hospitalDTOs, HttpStatus.OK);
    }

    @PostMapping
    @PreAuthorize("hasAuthority('CREATE_PRIVILEGE')")
    public ResponseEntity<HospitalDTO> createHospital(@RequestBody HospitalDTO dto) {
        HospitalDTO hospitalDTO = hospitalService.create(dto);

        return new ResponseEntity<HospitalDTO>(hospitalDTO, HttpStatus.OK);
    }


    @PutMapping
    @PreAuthorize("hasAuthority('UPDATE_PRIVILEGE')")
    public ResponseEntity<HospitalDTO> updateHospital(@RequestBody HospitalDTO dto) {
        HospitalDTO hospitalDTO = hospitalService.update(dto);

        return new ResponseEntity<HospitalDTO>(hospitalDTO, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('DELETE_PRIVILEGE')")
    public ResponseEntity<GenericMessageDTO> deleteHospital(@PathVariable long id) {
        hospitalService.delete(id);

        return new ResponseEntity<>(new GenericMessageDTO("Hospital with ID " + id + " successfully deleted."), HttpStatus.OK);
    }
}
