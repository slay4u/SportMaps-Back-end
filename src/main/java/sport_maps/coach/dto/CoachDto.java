package sport_maps.coach.dto;

public record CoachDto(Long id, String firstName, String lastName, Long age, Long experience,
        Double price, String description, String sportType, byte[] image) {}
