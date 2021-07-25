package com.epsih.rest;

import java.util.List;

import javax.validation.Valid;

import com.epsih.constants.Endpoints;
import com.epsih.model.user.Doctor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.epsih.model.service.BusinessService;
import com.epsih.service.BusinessServiceService;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@RestController
@RequestMapping(Endpoints.SERVICE_ROOT)
public class ServiceController {

	private final BusinessServiceService service;

	@GetMapping(Endpoints.SERVICE_ID)
	public BusinessService getBusinessTypes(@PathVariable Long id) {
		return service.businessServiceById(id);
	}

	@GetMapping
	public List<BusinessService> getAllBusinessTypes() {
		return service.allBusinessServices();
	}

	@PostMapping
	public void postNewBusinessType(@Valid @RequestBody BusinessService businessService) {
	   service.addNewBusinessService(businessService);
	}

	@DeleteMapping(Endpoints.SERVICE_ID)
	public void deleteBusinessType(@PathVariable Long id) {
		service.deleteBusinessType(id);
	}

	@PutMapping(Endpoints.SERVICE_ID)
	public void updateBusinessType(@PathVariable Long id, @RequestBody BusinessService newType) {
		service.updateById(id, newType);
	}

	@GetMapping(Endpoints.SERVICE_DOCTORS)
	public List<Doctor> getServiceDoctors(@PathVariable("id") Long serviceId) {
      return service.getServiceDoctors(serviceId);
   }

}
