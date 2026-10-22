package com.test.demo.controller;

import java.util.List;

import jakarta.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.test.demo.dto.TodoForm;
import com.test.demo.entity.Todo;
import com.test.demo.entity.Users;
import com.test.demo.interceptor.CurrentUserInterceptor;
import com.test.demo.repository.TodoRepository;
import com.test.demo.util.ExcelExport;

@Controller
public class TodoController {

	private final TodoRepository todoRepository;

	public TodoController(TodoRepository todoRepository) {
		this.todoRepository = todoRepository;
	}

	@GetMapping("/todo")
	public String list(@RequestAttribute(CurrentUserInterceptor.CURRENT_USER) Users user,
			@ModelAttribute TodoForm todoForm, Model model) {
		List<Todo> todos = todoRepository.findByUserEmailOrderByCreatedAtAscIdAsc(user.getEmail());
		model.addAttribute("todos", todos);
		model.addAttribute("completedCount", todos.stream().filter(Todo::isCompleted).count());
		return "todo";
	}

	@PostMapping("/todo")
	public String add(@RequestAttribute(CurrentUserInterceptor.CURRENT_USER) Users user,
			@Valid @ModelAttribute TodoForm todoForm, BindingResult result, Model model,
			RedirectAttributes redirectAttributes) {
		if (result.hasErrors()) {
			model.addAttribute("error", result.getFieldErrors().get(0).getDefaultMessage());
			return list(user, todoForm, model);
		}
		todoRepository.save(new Todo(user.getEmail(), todoForm.getTitle(), todoForm.getDueDate()));
		redirectAttributes.addFlashAttribute("message", "Task added to your list.");
		return "redirect:/todo";
	}

	@PostMapping("/todo/{id}/toggle")
	public String toggle(@RequestAttribute(CurrentUserInterceptor.CURRENT_USER) Users user, @PathVariable Long id) {
		todoRepository.findByIdAndUserEmail(id, user.getEmail()).ifPresent(todo -> {
			todo.setCompleted(!todo.isCompleted());
			todoRepository.save(todo);
		});
		return "redirect:/todo";
	}

	@PostMapping("/todo/{id}/delete")
	public String delete(@RequestAttribute(CurrentUserInterceptor.CURRENT_USER) Users user, @PathVariable Long id,
			RedirectAttributes redirectAttributes) {
		todoRepository.findByIdAndUserEmail(id, user.getEmail()).ifPresent(todo -> {
			todoRepository.delete(todo);
			redirectAttributes.addFlashAttribute("message", "Task removed.");
		});
		return "redirect:/todo";
	}

	@GetMapping("/todo/export")
	public ResponseEntity<byte[]> export(@RequestAttribute(CurrentUserInterceptor.CURRENT_USER) Users user) {
		List<List<String>> rows = todoRepository.findByUserEmailOrderByCreatedAtAscIdAsc(user.getEmail()).stream()
				.map(todo -> List.of(todo.getTitle(), todo.getDueDate() == null ? "" : todo.getDueDate().toString(),
						todo.isCompleted() ? "Completed" : "Pending"))
				.toList();
		return ExcelExport.download("todo-list.xlsx", "ToDo", List.of("Task", "Due date", "Status"), rows);
	}

}
