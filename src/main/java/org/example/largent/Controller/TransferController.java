package org.example.largent.Controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.largent.API.ApiResponse;
import org.example.largent.Model.Transfer;
import org.example.largent.Service.TransferService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/transfer")
@RequiredArgsConstructor
public class TransferController {

    private final TransferService transferService;

    @GetMapping("/get")
    public ResponseEntity getTransfers(){
        return ResponseEntity.ok(transferService.getTransfers());
    }

    @PostMapping("/add")
    public ResponseEntity addPurchHistory(@RequestBody @Valid Transfer transfer, Errors errors){
        if (errors.hasErrors()){
            String message=errors.getFieldError().getDefaultMessage();
            return ResponseEntity.badRequest().body(message);
        }

        transferService.addTransfer(transfer);
        return ResponseEntity.ok().body(new ApiResponse("Transfer has been made"));
    }


    @PutMapping("/update/{id}")
    public ResponseEntity updateTransfer(@PathVariable Integer id, @RequestBody @Valid Transfer transfer, Errors errors){
        if (errors.hasErrors()){
            String message=errors.getFieldError().getDefaultMessage();
            return ResponseEntity.badRequest().body(message);
        }
        transferService.updateTransfer(id,transfer);
        return ResponseEntity.ok().body(new ApiResponse("Transfer has been updated"));

    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity deleteTransfer(@PathVariable Integer id){
        transferService.deleteTransfer(id);
        return ResponseEntity.ok().body(new ApiResponse("Transfer has been deleted"));
    }

    //                  EXTRA

    @PostMapping("/transfer-currency/{transferId}/{currency}/{amount}")
    public ResponseEntity transferCurrency(@PathVariable Integer transferId,@PathVariable String currency,@PathVariable Double amount){
        transferService.transferCurrency(transferId,currency, amount);
        return ResponseEntity.ok().body(new ApiResponse("Transfer with the currency "+currency+" has been made"));
    }
}
