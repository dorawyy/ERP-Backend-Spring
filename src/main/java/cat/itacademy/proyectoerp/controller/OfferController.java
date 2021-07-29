package cat.itacademy.proyectoerp.controller;

import java.util.HashMap;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cat.itacademy.proyectoerp.domain.Offer;
import cat.itacademy.proyectoerp.dto.OfferDTO;
import cat.itacademy.proyectoerp.service.IOfferService;

@RestController
@RequestMapping("/api/offers")
public class OfferController {
	
	@Autowired
	IOfferService offerService ;
	
	@GetMapping
	public HashMap<String, Object> getOffers() {

		HashMap<String, Object> map = new HashMap<String, Object>();

		try {

			List<OfferDTO> OfferList = offerService.findAll();

			map.put("success", "true");
			map.put("message", "Offers found");
			map.put("Offers", OfferList);

		} catch (Exception e) {
			map.put("success", "false");
			map.put("message", "error: " + e.getMessage());
		}

		return map;
	}
	
	@PreAuthorize("hasRole('ADMIN')")
	@PostMapping
	public HashMap<String, Object> createOffer( @Valid @RequestBody Offer offer) {
		
		HashMap<String, Object> map = new HashMap<String, Object>();
		
		try {
			OfferDTO offerDTO = offerService.createOffer(offer);
			map.put("success", "true");
			map.put("message", "offer created");
			map.put("offer",offerDTO);
		} catch (Exception e) {
			map.put("success", "false");
			map.put("message", "error: " + e.getMessage());
		}
		return map;
	}

}
