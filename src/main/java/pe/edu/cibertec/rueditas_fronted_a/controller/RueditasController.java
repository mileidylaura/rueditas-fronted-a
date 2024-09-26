package pe.edu.cibertec.rueditas_fronted_a.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import pe.edu.cibertec.rueditas_fronted_a.dto.MatriculaRequestDTO;
import pe.edu.cibertec.rueditas_fronted_a.dto.MatriculaResponseDTO;
import pe.edu.cibertec.rueditas_fronted_a.viewmodel.RueditasModel;

@Controller
@RequestMapping("/rueditas")
public class RueditasController {
    @Autowired
    RestTemplate restTemplate;
    @GetMapping("/inicio")
    public String inicio(Model model) {
        RueditasModel rueditasModel = new RueditasModel("00", "", "", "", "", "", "");
        model.addAttribute("RueditasModel", rueditasModel);

        return "inicio";

    }

    @PostMapping("/buscarplaca")
    public String buscarVehiculo(@RequestParam String nroMatricula, Model model) {

        if (nroMatricula.matches("^[A-Za-z0-9]{3}-[A-Za-z0-9]{3}$")) {
            try {
                String URL = "http://localhost:8081/vehiculo/obtener" ;
                MatriculaRequestDTO matriculaRequestDTO =  new MatriculaRequestDTO(nroMatricula);
                 MatriculaResponseDTO matriculaResponseDTO = restTemplate.postForObject(URL, matriculaRequestDTO, MatriculaResponseDTO.class);

                if (matriculaResponseDTO.codigo().equals("00")){
                    RueditasModel rueditasModel = new RueditasModel("00","",matriculaResponseDTO.marca(),matriculaResponseDTO.modelo(),matriculaResponseDTO.nasientos(),matriculaResponseDTO.precio(),matriculaResponseDTO.color());
                    model.addAttribute("RueditasModel", rueditasModel);

                    return "resultados";
                }

                RueditasModel rueditasModel = new RueditasModel("01","No existe vehiculo", "", "", "", "", "");
                model.addAttribute("RueditasModel", rueditasModel);

                return "inicio";

            }catch (Exception e){

                RueditasModel rueditasModel = new RueditasModel("99", "Error : "+e.getMessage(), "", "", "", "", "");
                model.addAttribute("RueditasModel", rueditasModel);

                return "inicio";
            }
        } else {
            RueditasModel rueditasModel = new RueditasModel("01", "Debe ingresar una placa correcta", "", "", "", "", "");
            model.addAttribute("RueditasModel", rueditasModel);

            return "inicio";

        }
    }
}