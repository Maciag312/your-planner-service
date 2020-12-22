package maciag.controller;


import io.swagger.annotations.*;
import maciag.dto.CreateTaskDTO;
import maciag.dto.ModifyTaskDTO;
import maciag.dto.TaskDTO;
import maciag.repository.UserRepository;
import maciag.service.TaskService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;


@RestController
@RequestMapping("/tasks")
@Api(tags = "tasks")
public class TaskController {


    TaskService taskService;
    UserRepository userRepository;

    Logger logger = LoggerFactory.getLogger(TaskController.class);

    public TaskController(TaskService taskService, UserRepository userRepository){
        this.taskService = taskService;
        this.userRepository = userRepository;
    }

    private maciag.model.User fetchUser(Authentication authentication){
        try {
            User user = (User) authentication.getPrincipal();
            maciag.model.User userFetched = userRepository.findByUsername(user.getUsername()).get();
            return userFetched;
        } catch (NoSuchElementException exception){
            logger.warn("User wasn't fetched " + exception);
            return null;
        }
    }


    @PostMapping
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_CLIENT')")
    @ApiOperation(value = "${TaskController.addTask}")
    @ApiResponses(value = {@ApiResponse(code = 400, message = "Something went wrong"),   @ApiResponse(code = 403, message = "Access denied"),  @ApiResponse(code = 404, message = "The user doesn't exist"), @ApiResponse(code = 500, message = "Expired or invalid JWT token")})
    public void addTask(@ApiParam(name="Task", required = true) @RequestBody CreateTaskDTO taskDTO, @ApiIgnore Authentication authentication) {
        taskService.addTask(fetchUser(authentication), taskDTO);
    }



    @GetMapping
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_CLIENT')")
    @ApiOperation(value = "${TaskController.getAllTasks}")
    @ApiResponses(value = {@ApiResponse(code = 400, message = "Something went wrong"),   @ApiResponse(code = 403, message = "Access denied"),  @ApiResponse(code = 404, message = "The user doesn't exist"), @ApiResponse(code = 500, message = "Expired or invalid JWT token")})
    public List<TaskDTO> getAllTasks(@ApiIgnore Authentication authentication) {
        return taskService.getAllTasks(fetchUser(authentication));
    }

    @GetMapping(value = "/{date}")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_CLIENT')")
    @ApiOperation(value = "${TaskController.getAllTasksByDate}")
    @ApiResponses(value = {@ApiResponse(code = 400, message = "Something went wrong"),   @ApiResponse(code = 403, message = "Access denied"),  @ApiResponse(code = 404, message = "The user doesn't exist"), @ApiResponse(code = 500, message = "Expired or invalid JWT token")})
    public List<TaskDTO> getAllTasksByDate(@PathVariable String date, @ApiIgnore Authentication authentication) {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        try {
            return taskService.getAllTasksByDate(fetchUser(authentication), dateFormat.parse(date));
        }catch(ParseException p){
            logger.warn("Parse exception improper parse of date");
            return new ArrayList<>();
        }
    }


    @PutMapping(consumes = "application/json")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_CLIENT')")
    @ApiOperation(value = "${TaskController.modifyTask}")
    @ApiResponses(value = {@ApiResponse(code = 400, message = "Something went wrong"),   @ApiResponse(code = 403, message = "Access denied"),  @ApiResponse(code = 404, message = "The user doesn't exist"), @ApiResponse(code = 500, message = "Expired or invalid JWT token")})
    public void modifyTask(@RequestBody ModifyTaskDTO taskDTO, @ApiIgnore Authentication authentication) {
        taskService.modifyTask(taskDTO, fetchUser(authentication));
    }


    @PutMapping(value = "/{task_id}/toggleIsDone", consumes = "application/json")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_CLIENT')")
    @ApiOperation(value = "${TaskController.toggleIsDone}")
    @ApiResponses(value = {@ApiResponse(code = 400, message = "Something went wrong"),   @ApiResponse(code = 403, message = "Access denied"),  @ApiResponse(code = 404, message = "The user doesn't exist"), @ApiResponse(code = 500, message = "Expired or invalid JWT token")})
    public String toggleIsDone(@PathVariable Long task_id, @ApiIgnore Authentication authentication) {
        return taskService.toggleIsDoneAndShowState(task_id, fetchUser(authentication));
    }

    @DeleteMapping("/{task_id}")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_CLIENT')")
    @ApiOperation(value = "${TaskController.removeTask}")
    @ApiResponses(value = {@ApiResponse(code = 400, message = "Something went wrong"),   @ApiResponse(code = 403, message = "Access denied"),  @ApiResponse(code = 404, message = "The user doesn't exist"), @ApiResponse(code = 500, message = "Expired or invalid JWT token")})
    public void removeTask(@PathVariable Long task_id, @ApiIgnore Authentication authentication) {
        taskService.removeTask(task_id, fetchUser(authentication));
    }


    @PostMapping("/{task_id}/run")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_CLIENT')")
    @ApiOperation(value = "${TaskController.runTask}")
    @ApiResponses(value = {@ApiResponse(code = 400, message = "Something went wrong"),   @ApiResponse(code = 403, message = "Access denied"),  @ApiResponse(code = 404, message = "The user doesn't exist"), @ApiResponse(code = 500, message = "Expired or invalid JWT token")})
    public void runTask(@PathVariable Long task_id, @ApiIgnore Authentication authentication) {
        taskService.runTask(task_id, fetchUser(authentication));
    }

    @PostMapping("/{task_id}/stop")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_CLIENT')")
    @ApiOperation(value = "${TaskController.stopTask}")
    @ApiResponses(value = {@ApiResponse(code = 400, message = "Something went wrong"),   @ApiResponse(code = 403, message = "Access denied"),  @ApiResponse(code = 404, message = "The user doesn't exist"), @ApiResponse(code = 500, message = "Expired or invalid JWT token")})
    public void stopTask(@PathVariable Long task_id, @ApiIgnore Authentication authentication) {
        taskService.stopTask(task_id, fetchUser(authentication));
    }


    @GetMapping("/{task_id}/calculateAndGetDuration")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_CLIENT')")
    @ApiOperation(value = "${TaskController.calculateAndGetDuration}")
    @ApiResponses(value = {@ApiResponse(code = 400, message = "Something went wrong"),   @ApiResponse(code = 403, message = "Access denied"),  @ApiResponse(code = 404, message = "The user doesn't exist"), @ApiResponse(code = 500, message = "Expired or invalid JWT token")})
    public void calculateAndGetDuration(@PathVariable Long task_id, @ApiIgnore Authentication authentication) {
        taskService.calculateAndGetDuration(task_id, fetchUser(authentication));
    }

    @GetMapping("/{task_id}/isTaskRunning")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_CLIENT')")
    @ApiOperation(value = "${TaskController.isTaskRunning}")
    @ApiResponses(value = {@ApiResponse(code = 400, message = "Something went wrong"),   @ApiResponse(code = 403, message = "Access denied"),  @ApiResponse(code = 404, message = "The user doesn't exist"), @ApiResponse(code = 500, message = "Expired or invalid JWT token")})
    public boolean isTaskRunning(@PathVariable Long task_id, @ApiIgnore Authentication authentication) {
        return taskService.isTaskRunning(task_id, fetchUser(authentication));
    }



}
