package com.meddoc.meddoc.controller;

import com.meddoc.meddoc.model.Medicamento;
import com.meddoc.meddoc.repository.MedicamentoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;


@Controller
public class CalendarController {

    @Autowired
    private MedicamentoRepository medicamentoRepository;

    @GetMapping("idoso/calendario")
    public String calendario(Model model,
                             @RequestParam(value = "month", required = false) Integer month,
                             @RequestParam(value = "year", required = false) Integer year) {

        List<Medicamento> medicamentos = medicamentoRepository.findAll();
        model.addAttribute("medicamentos", medicamentos);

        LocalDate today = LocalDate.now();

        YearMonth currentMonth;
        if (month != null && year != null) {
            currentMonth = YearMonth.of(year, month);
        } else {
            currentMonth = YearMonth.from(today);
        }

        model.addAttribute("today", today);
        model.addAttribute("currentMonthYear", currentMonth.getMonth().getDisplayName(java.time.format.TextStyle.FULL, new java.util.Locale("pt", "BR")) + " " + currentMonth.getYear());
        model.addAttribute("year", currentMonth.getYear());
        model.addAttribute("monthValue", currentMonth.getMonthValue());
        model.addAttribute("numberOfDaysInMonth", currentMonth.lengthOfMonth());

        LocalDate firstDayOfMonth = currentMonth.atDay(1);
        int dayOfWeekForFirstDay = firstDayOfMonth.getDayOfWeek().getValue();

        int firstDayOfWeekAdjusted = (dayOfWeekForFirstDay == 7) ? 0 : dayOfWeekForFirstDay;

        model.addAttribute("firstDayOfWeek", firstDayOfWeekAdjusted);

        return "idoso/calendario";
    }
} 
