package com.example.la_mia_pizzeria_crud.controllers;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.la_mia_pizzeria_crud.model.Pizza;
import com.example.la_mia_pizzeria_crud.repository.PizzaRepository;

import jakarta.annotation.Generated;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/pizzas")
public class PizzaController {

    @Autowired // spring initetta in automatico il repository
    private PizzaRepository pizzaRepository;

    @GetMapping
    public String index(Model model) {

        // recupero tutte le pizze dal db
        List<Pizza> pizzas = pizzaRepository.findAll();
        // e le pesso al model con nome "pizzas"
        model.addAttribute("pizzas", pizzas);

        // infine restituisco il nome del template thymeleaf
        return "pizzas/index";
    }

    @GetMapping("/{id}") // restituisce GET /pizza/{id}
    public String show(@PathVariable Integer id, Model model) {

        // cerco la pizza tramite repository
        Optional<Pizza> pizza = pizzaRepository.findById(id);

        // controllo se esiste
        if (pizza.isPresent()) {
            model.addAttribute("pizza", pizza.get());
            return "pizzas/show";
        } else {
            // se non esiste reindirizzo alla lista
            return "redirect:/pizzas";
        }
    }

    @GetMapping("/create")
    public String create(Model model) {

        model.addAttribute("pizza", new Pizza());
        return "pizzas/create";
    }

    @PostMapping("/create")
    public String store(@Valid @ModelAttribute("pizza") Pizza formPizza, BindingResult bindingResult, Model model) {
        // 1. controllo errori di validazione
        if (bindingResult.hasErrors()) {
            // se ci sono, ritorna la view del form senza salvare
            return "pizzas/create";
        } else {
            // se non ci sono errrori salvo e reindirizzo
            pizzaRepository.save(formPizza);
            return "redirect:/pizzas";
        }
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable Integer id, Model model) {
        model.addAttribute("pizza", pizzaRepository.findById(id).get());
        return "pizzas/edit";
    }

    @PostMapping("/edit/{id}")
    public String upadate(@Valid @ModelAttribute("pizza") Pizza formPizza, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            return "pizzas/edit";
        } else {
            pizzaRepository.save(formPizza);
            return "redirect:/pizzas";
        }
    }

    @PostMapping("/delete/{id}")
    // il delete non ha bisogno del model
    public String delete(@PathVariable Integer id) {
        pizzaRepository.deleteById(id);
        return "redirect:/pizzas";
    }

}
