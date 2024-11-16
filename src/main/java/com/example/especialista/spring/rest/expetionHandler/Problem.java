
package com.example.especialista.spring.rest.expetionHandler;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)/*só inclui o atributo no JSON se ele não estiver null*/
public class Problem {

    private Integer status;
    private String type;
    private String title;
    private String detail;
    private String userMessage; /*MSG para o usuario*/
    private LocalDateTime timestamp;
    private List<Field> fields;

    public Problem(List<Field> fields) {
        this.fields = fields;
    }

    public Problem(Integer status, String type, String title, String detail, String userMessage, LocalDateTime timestamp, List<Field> fields) {
        this.status = status;
        this.type = type;
        this.title = title;
        this.detail = detail;
        this.userMessage = userMessage;
        this.timestamp = timestamp;
        this.fields = fields;
    }

    public Problem() {

    }

    public String getUserMessage() {
        return userMessage;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public void setUserMessage(String userMessage) {
        this.userMessage = userMessage;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public void setFields(List<Field> fields) {
        this.fields = fields;
    }

    public List<Field> getFields() {
        return fields;
    }

    @Getter
    public static class Field{
        private String name;
        private String userMessage;

        public Field() {

        }

        public Field name(String name){
            this.name = name;
            return this;
        }
        public Field userMessage(String userMessage){
            this.userMessage = userMessage;
            return this;
        }
        public Field build(){
            Field field = new Field();
            field.name(this.name);
            field.userMessage(this.userMessage);
            return field;
        }

        public Field(String name, String userMessage) {
            this.name = name;
            this.userMessage = userMessage;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getUserMessage() {
            return userMessage;
        }

        public void setUserMessage(String userMessage) {
            this.userMessage = userMessage;
        }

        public static Field builder(){
            return new Field();
        }
    }

    public static class ProblemBuilder {
        private Integer status;
        private String type;
        private String title;
        private String detail;

        /*agora o ideal é que todas as respostas tenham um userMessage*/
        private String userMessage;

        private LocalDateTime timestamp;
        private List<Field> fields;

        public ProblemBuilder timestamp(LocalDateTime timestamp){
            this.timestamp = timestamp;
            return this;
        }

        public ProblemBuilder status(Integer status) {
            this.status = status;
            return this;
        }

        public ProblemBuilder type(String type) {
            this.type = type;
            return this;
        }

        public ProblemBuilder title(String title) {
            this.title = title;
            return this;
        }

        public ProblemBuilder detail(String detail) {
            this.detail = detail;
            return this;
        }

        public ProblemBuilder userMessage(String userMessage){
            this.userMessage = userMessage;
            return this;
        }

        public ProblemBuilder fields (List<Field> fields){
            this.fields = fields;
            return this;
        }

        public Problem build() {
            Problem problem = new Problem();
            problem.setTimestamp(this.timestamp);
            problem.setStatus(this.status);
            problem.setType(this.type);
            problem.setTitle(this.title);
            problem.setDetail(this.detail);
            problem.setUserMessage(this.userMessage);
            problem.setFields(this.fields);

            return problem;
        }
    }

    public static ProblemBuilder builder() {
        return new ProblemBuilder();
    }
}
