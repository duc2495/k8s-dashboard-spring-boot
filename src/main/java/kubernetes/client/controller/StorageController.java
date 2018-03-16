package kubernetes.client.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import kubernetes.client.model.Project;
import kubernetes.client.model.Storage;
import kubernetes.client.service.ProjectService;
import kubernetes.client.service.StorageService;
import kubernetes.client.validator.StorageValidator;

@Controller
public class StorageController extends BaseController {

	@Autowired
	private ProjectService projectService;

	@Autowired
	private StorageService storageService;

	@Autowired
	private StorageValidator storageValidator;

	@RequestMapping(value = "/project/{name}/storage", method = RequestMethod.GET)
	public String listStorage(@PathVariable String name, Model model) {
		Project project = projectService.getProjectByName(name, getCurrentUser().getCustomer().getId());
		if (project == null) {
			return "403";
		}
		model.addAttribute("project", project);
		List<Storage> storageList = storageService.getStorageByProjectName(project.getProjectName());
		model.addAttribute("storageList", storageList);
		return "storage/storage";
	}

	@RequestMapping(value = "/project/{name}/storage/new", method = RequestMethod.GET)
	public String createStorageForm(@PathVariable String name, Model model) {
		Project project = projectService.getProjectByName(name, getCurrentUser().getCustomer().getId());
		if (project == null) {
			return "403";
		}
		model.addAttribute("project", project);
		Storage storage = new Storage();
		model.addAttribute("storage", storage);
		return "storage/create_storage";
	}

	@RequestMapping(value = "/project/{name}/storage", method = RequestMethod.POST)
	public String createStorage(@Valid @ModelAttribute("storage") Storage storage, @PathVariable String name,
			BindingResult result, Model model, RedirectAttributes redirectAttributes) {
		Project project = projectService.getProjectByName(name, getCurrentUser().getCustomer().getId());
		if (project == null) {
			return "403";
		}
		model.addAttribute("project", project);
		storageValidator.validate(storage, result);
		if (result.hasErrors()) {
			return "storage/create_storage";
		}

		if (storageService.storageExists(storage.getName(), name)) {
			result.rejectValue("name", "error.exists", new Object[] { name },
					"Application " + name + " already exists");
			return "storage/create_storage";
		}
		storageService.insert(storage, name);
		redirectAttributes.addFlashAttribute("info", "Storage created successfully");
		return "redirect:/project/" + name + "/storage";
	}
	
	@RequestMapping(value = "/project/{name}/storage/{storageName}", method = RequestMethod.DELETE)
	public String deleteProject(@PathVariable("name") String name, @PathVariable("storageName") String storageName, Model model, RedirectAttributes redirectAttributes) {
		Project project = projectService.getProjectByName(name, getCurrentUser().getCustomer().getId());
		if (project == null) {
			return "403";
		}
		storageService.delete(storageName, name);
		redirectAttributes.addFlashAttribute("info", "Storage deleted successfully");
		return "redirect:/project/" + name + "/storage";
	}
}
