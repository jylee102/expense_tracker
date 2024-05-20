package com.expenseTracker.controller;

import com.expenseTracker.constant.Type;
import com.expenseTracker.dto.MemberFormDto;
import com.expenseTracker.dto.TransactionFormDto;
import com.expenseTracker.entity.Asset;
import com.expenseTracker.entity.Category;
import com.expenseTracker.entity.Member;
import com.expenseTracker.entity.Transaction;
import com.expenseTracker.service.AssetService;
import com.expenseTracker.service.CategoryService;
import com.expenseTracker.service.MemberService;
import com.expenseTracker.service.TransactionService;
import com.expenseTracker.util.TransactionSerializer;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.security.Principal;
import java.util.*;

@Controller
@RequiredArgsConstructor
public class MainController {

    private final TransactionService transactionService;

    private final MemberService memberService;

    private final AssetService assetService;
    private final CategoryService categoryService;

    @GetMapping(value = "/")
    public String main(HttpServletRequest request, Model model, Principal principal,
                       @RequestParam(value = "searchValue", defaultValue = "") String searchValue) {
        // 로그인 되어 있지 않다면 로그인 페이지로
        Object httpStatus = request.getAttribute("HttpStatus");
        if (httpStatus != null && (int) httpStatus == HttpServletResponse.SC_UNAUTHORIZED)
            return "/members/login";

        Member member = memberService.getMember(principal.getName());
        loadInitialData(model, member, searchValue);

        return "index";
    }

    @GetMapping(value = {"/loadItems", "/loadItems/{page}"})
    @ResponseBody
    public Page<Transaction> loadItems(Principal principal, @RequestParam("page") int page,
                                       @RequestParam("year") int year, @RequestParam("month") int month,
                                       @RequestParam("searchValue") String searchValue) throws Exception {
        Long memberId = memberService.getMember(principal.getName()).getId();// 현재 로그인한 유저
        return transactionService.getPage(memberId, year, month, searchValue, PageRequest.of(page, 10));
    }

    private void loadInitialData(Model model, Member member, String searchValue) {
        try {

            Long memberId = member.getId();
            model.addAttribute("member", memberId);

            List<Asset> assets = assetService.getAssets(memberId);
            List<Category> incomeCategories = categoryService.getCategories(memberId, Type.INCOME);
            List<Category> expenseCategories = categoryService.getCategories(memberId, Type.EXPENSE);

            if (assets.isEmpty() || incomeCategories.isEmpty() || expenseCategories.isEmpty()) {
                model.addAttribute("errorMessage",
                        "설정/관리를 확인해주세요.\n(카테고리들은 각각 적어도 하나씩 존재해야 합니다.)");
            }
            model.addAttribute("assets", assets);
            model.addAttribute("incomeCategories", incomeCategories);
            model.addAttribute("expenseCategories", expenseCategories);

            model.addAttribute("transactionFormDto", new TransactionFormDto());
            model.addAttribute("searchValue", searchValue);
        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("errorMessage", "데이터를 불러오는 것에 실패했습니다. 관리자에게 문의하세요.");
        }
    }

    @GetMapping(value = "/view/calendar")
    public String calendar(HttpServletRequest request,
                           Model model, Principal principal) throws Exception {
        // 로그인 되어 있지 않다면 로그인 페이지로
        Object httpStatus = request.getAttribute("HttpStatus");
        if (httpStatus != null && (int) httpStatus == HttpServletResponse.SC_UNAUTHORIZED)
            return "/members/login";

        return "calendar";
    }

    @GetMapping(value = "/getMomentData")
    public @ResponseBody ResponseEntity getMomentData(@RequestParam("year") int year, @RequestParam("month") int month,
                                                      Principal principal) {
        try {
            // Gson 객체 생성 시 커스텀 어댑터 등록
            Gson gson = new GsonBuilder()
                    .registerTypeAdapter(Transaction.class, new TransactionSerializer())
                    .create();

            Long memberId = memberService.getMember(principal.getName()).getId();// 현재 로그인한 유저

            List<Transaction> transactions = transactionService.getList(memberId, year, month);
            String json = gson.toJson(transactions); // List<Transaction>을 JSON 문자열로 변환

            return new ResponseEntity<>(json, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>("데이터를 불러오는 것에 실패했습니다. 관리자에게 문의해주세요.", HttpStatus.BAD_REQUEST);
        }

    }

    @GetMapping(value = "/view/chart")
    public String chart(HttpServletRequest request) {
        // 로그인 되어 있지 않다면 로그인 페이지로
        Object httpStatus = request.getAttribute("HttpStatus");
        if (httpStatus != null && (int) httpStatus == HttpServletResponse.SC_UNAUTHORIZED)
            return "/members/login";

        return "chart";
    }

    @GetMapping(value = "/getStatistics")
    public @ResponseBody ResponseEntity getStatistics(@RequestParam("year") int year, @RequestParam("month") int month,
                                                      @RequestParam("type") String type, Principal principal) {

        List<Category> categories = null;
        List<Map<String, Object>> chartData = null;
        double total = 0;

        try {
            Long memberId = memberService.getMember(principal.getName()).getId();// 현재 로그인한 유저

            if (type.equals("income")) {
                categories = categoryService.getCategories(memberId, Type.INCOME);
                categories.add(categoryService.findById(2L));

                // 수입 카테고리 내역의 총액
                total = transactionService.getTotalAmountOf(Type.INCOME, year, month, memberId);
            } else {
                categories = categoryService.getCategories(memberId, Type.EXPENSE);
                categories.add(categoryService.findById(1L));

                // 지출 카테고리 내역의 총액
                total = transactionService.getTotalAmountOf(Type.EXPENSE, year, month, memberId);
            }

            // 각 카테고리별 내역 액수를 계산
            List<Integer> amounts = new ArrayList<>();
            for (Category category : categories) {
                int amount = transactionService.calcTotalAmountForCategory(category, year, month);
                amounts.add(amount);
            }

            // 카테고리 이름과 금액 데이터를 매핑하여 차트에 사용할 수 있는 형태로 변환
            chartData = convertToChartData(categories, amounts, total);

            Map<String, Object> statistics = new HashMap<>();
            statistics.put("chartData", chartData);
            statistics.put("total", total);

            return new ResponseEntity<>(statistics, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>("데이터를 불러오는 것에 실패했습니다. 관리자에게 문의해주세요.", HttpStatus.BAD_REQUEST);
        }
    }

    // 카테고리 이름과 수입 데이터를 매핑하여 차트에 사용할 수 있는 형태로 변환하는 메서드
    private List<Map<String, Object>> convertToChartData(List<Category> categories, List<Integer> amounts,
                                                         double total) {
        List<Map<String, Object>> chartData = new ArrayList<>();
        int categoriesSize = categories.size();
        int amountsSize = amounts.size();
        for (int i = 0; i < categoriesSize && i < amountsSize; i++) {
            String categoryName = categories.get(i).getName();
            int amount = amounts.get(i);
            double data = amount / total * 100;
            String color = categories.get(i).getColor();

            Map<String, Object> map = new HashMap<>();
            map.put("label", categoryName);
            map.put("data", data);
            map.put("color", color);
            map.put("amount", amount);
            chartData.add(map);
        }
        chartData.sort((a, b) -> Double.compare((double) b.get("data"), (double) a.get("data"))); // data를 기준으로 내림차순으로(data가 큰 순서대로)
        return chartData;
    }

    @GetMapping(value = "/settings/assets")
    public String assets(Model model, Principal principal) {
        try {
            Long memberId = memberService.getMember(principal.getName()).getId();
            List<Asset> assets = assetService.getAssets(memberId);

            model.addAttribute("assets", assets);
        } catch (Exception e) {
            return "/members/login/error";
        }
        return "settings/assets";
    }

    @GetMapping(value = "/settings/categories")
    public String categories(Model model, Principal principal) {
        try {
            Long memberId = memberService.getMember(principal.getName()).getId();
            List<Category> incomeCategories = categoryService.getCategories(memberId, Type.INCOME);
            List<Category> expenseCategories = categoryService.getCategories(memberId, Type.EXPENSE);
            model.addAttribute("incomeCategories", incomeCategories);
            model.addAttribute("expenseCategories", expenseCategories);
        } catch (Exception e) {
            return "/members/login/error";
        }
        return "settings/categories";
    }

    @GetMapping(value = "/settings/myInfo")
    public String myInfo(Model model, Principal principal) {
        Member member = memberService.getMember(principal.getName());
        model.addAttribute("memberFormDto", MemberFormDto.of(member));
        return "settings/myInfo";
    }
}