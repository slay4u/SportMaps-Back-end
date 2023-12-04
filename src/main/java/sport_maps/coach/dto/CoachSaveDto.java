package sport_maps.coach.dto;

public record CoachSaveDto(String firstName, String lastName, Long age, Long experience,
        Double price, String description, String sportType) {}
