package org.example.largent.Controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.largent.API.ApiResponse;
import org.example.largent.Model.Plan;
import org.example.largent.Service.PlanService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/plan")
@RequiredArgsConstructor
public class PlanController {

    private final PlanService planService;

    @GetMapping("/get")
    public ResponseEntity getPlans(){
        return ResponseEntity.ok().body(planService.getAllPlans());
    }

    @PostMapping("/add")
    public ResponseEntity addPlan(@RequestBody @Valid Plan plan, Errors errors){
        if (errors.hasErrors()){
            String message=errors.getFieldError().getDefaultMessage();
            return ResponseEntity.badRequest().body(message);
        }
        planService.addPlan(plan);

        return ResponseEntity.ok().body(new ApiResponse("Plan has been made"));
    }


    @PutMapping("/update/{id}")
    public ResponseEntity updatePlan(@PathVariable Integer id, @RequestBody @Valid Plan plan, Errors errors){
        if (errors.hasErrors()){
            String message=errors.getFieldError().getDefaultMessage();
            return ResponseEntity.badRequest().body(message);
        }
        planService.updatePlan(id,plan);

        return ResponseEntity.ok().body(new ApiResponse("Plan has been updated"));

    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity deletePlan(@PathVariable Integer id){
        planService.deletePlan(id);
        return ResponseEntity.ok().body(new ApiResponse("Plan has been deleted"));

    }

    //                    EXTRA

    @PostMapping("/add-amount/{title}/{amount}")
    public ResponseEntity addAmount(@PathVariable String title, @PathVariable Double amount){
        planService.addAmount(title, amount);
        return ResponseEntity.ok().body(new ApiResponse(amount+" has been added to plan "+title));

    }

    @GetMapping("/get-status/{status}")
    public ResponseEntity filterByStatus(@PathVariable String status){
        return ResponseEntity.ok().body(planService.filterByStatus(status));

    }
}
