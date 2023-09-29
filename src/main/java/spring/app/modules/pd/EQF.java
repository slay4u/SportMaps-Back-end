package spring.app.modules.pd;

import spring.app.modules.commons.constant.BaseConst;

public class EQF extends BaseConst {
    public static final EQF LEVEL_1 = new EQF(1, "Basic general knowledge", "basic skills required to carry out simple tasks", "work or study under direct supervision in a structured context");
    public static final EQF LEVEL_2 = new EQF(2, "Basic factual knowledge of a field of work or study", "basic cognitive and practical skills required to use relevant information in order to carry out tasks and to solve routine problems using simple rules and tools", "work or study under supervision with some autonomy");
    public static final EQF LEVEL_3 = new EQF(3, "Knowledge of facts, principles, processes and general concepts, in a field of work or study", "a range of cognitive and practical skills required to accomplish tasks and solve problems by selecting and applying basic methods, tools, materials and information", "take responsibility for completion of tasks in work or study; adapt own behaviour to circumstances in solving problems");
    public static final EQF LEVEL_4 = new EQF(4, "Factual and theoretical knowledge in broad contexts within a field of work or study", "a range of cognitive and practical skills required to generate solutions to specific problems in a field of work or study", "exercise self-management within the guidelines of work or study contexts that are usually predictable, but are subject to change; supervise the routine work of others, taking some responsibility for the evaluation and improvement of work or study activities");
    public static final EQF LEVEL_5 = new EQF(5, "Comprehensive, specialised, factual and theoretical knowledge within a field of work or study and an awareness of the boundaries of that knowledge", "a comprehensive range of cognitive and practical skills required to develop creative solutions to abstract problems", "exercise management and supervision in contexts of work or study activities where there is unpredictable change; review and develop performance of self and others");
    public static final EQF LEVEL_6 = new EQF(6, "Advanced knowledge of a field of work or study, involving a critical understanding of theories and principles", "advanced skills, demonstrating mastery and innovation, required to solve complex and unpredictable problems in a specialised field of work or study", "manage complex technical or professional activities or projects, taking responsibility for decision-making in unpredictable work or study contexts; take responsibility for managing professional development of individuals and groups");
    public static final EQF LEVEL_7 = new EQF(7, "Highly specialised knowledge, some of which is at the forefront of knowledge in a field of work or study, as the basis for original thinking and/or research. Critical awareness of knowledge issues in a field and at the interface between different fields", "specialised problem-solving skills required in research and/or innovation in order to develop new knowledge and procedures and to integrate knowledge from different fields", "manage and transform work or study contexts that are complex, unpredictable and require new strategic approaches; take responsibility for contributing to professional knowledge and practice and/or for reviewing the strategic performance of teams");
    public static final EQF LEVEL_8 = new EQF(8, "Knowledge at the most advanced frontier of a field of work or study and at the interface between fields", "the most advanced and specialised skills and techniques, including synthesis and evaluation, required to solve critical problems in research and/or innovation and to extend and redefine existing knowledge or professional practice", "demonstrate substantial authority, innovation, autonomy, scholarly and professional integrity and sustained commitment to the development of new ideas or processes at the forefront of work or study contexts including research");
    public static final EQF UNDEFINED = new EQF(UNDEFINED_KEY);

    private final String knowledge;
    private final String skills;
    private final String responsibility;

    private EQF(int code, String knowledge, String skills, String responsibility) {
        super(code);
        this.knowledge = knowledge;
        this.skills = skills;
        this.responsibility = responsibility;
    }

    private EQF(int code) {
        super(code);
        this.knowledge = "";
        this.skills = "";
        this.responsibility = "";
    }

    public String getKnowledge() {
        return knowledge;
    }

    public String getSkills() {
        return skills;
    }

    public String getResponsibility() {
        return responsibility;
    }
}
