package ru.practicum;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.service.StatsService;

import java.util.List;

@RestController
@RequestMapping
public class StatsController {
    private final StatsService statsService;

    @Autowired
    public StatsController(StatsService statsService) {
        this.statsService = statsService;
    }

    @PostMapping({"/hit"})
    @ResponseStatus(HttpStatus.CREATED)
    public void addRequest(@RequestBody RequestInDto requestInDto) {
        statsService.saveRequest(requestInDto);
    }

    @GetMapping({"/stats"})
    public List<RequestHitsOutDto> getStats(@RequestParam(required = false) String start,
                                            @RequestParam(required = false) String end,
                                            @RequestParam(required = false) List<String> uris,
                                            @RequestParam(defaultValue = "false") boolean unique) {
        return statsService.getStats(start, end, uris, unique);
    }
}
