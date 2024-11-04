package com.example.especialista.spring.rest.expetionHandler;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

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


    public static class ProblemBuilder {
        private Integer status;
        private String type;
        private String title;
        private String detail;

        /*agora o ideal é que todas as respostas tenham um userMessage*/
        private String userMessage;

        private LocalDateTime timestamp;

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


        public Problem build() {
            Problem problem = new Problem();
            problem.setTimestamp(this.timestamp);
            problem.setStatus(this.status);
            problem.setType(this.type);
            problem.setTitle(this.title);
            problem.setDetail(this.detail);
            problem.setUserMessage(this.userMessage);
            return problem;
        }
    }

    public static ProblemBuilder builder() {
        return new ProblemBuilder();
    }
}