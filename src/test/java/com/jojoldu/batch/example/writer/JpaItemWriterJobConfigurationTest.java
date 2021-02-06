package com.jojoldu.batch.example.writer;

import com.jojoldu.batch.TestBatchConfig;
import com.jojoldu.batch.entity.student.StudentRepository;
import com.jojoldu.batch.entity.student.Teacher;
import com.jojoldu.batch.entity.student.TeacherRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@SpringBatchTest
@SpringBootTest(classes={JpaItemWriterJobConfig.class, TestBatchConfig.class})
public class JpaItemWriterJobConfigurationTest {

    @Autowired
    private JobLauncherTestUtils jobLauncherTestUtils;

    @Autowired
    private TeacherRepository teacherRepository;

    @Autowired
    private StudentRepository studentRepository;

    @AfterEach
    void tearDown() {
        teacherRepository.deleteAll();
    }

    @SuppressWarnings("Duplicates")
    @Test
    public void JpaItemWriter테스트() throws Exception {

        //given
        for(long i=1;i<=10;i++) {
            String teacherName = i + "선생님";
            Teacher teacher = new Teacher(teacherName, "수학");
            teacherRepository.save(teacher);
        }

        JobParameters jobParameters = jobLauncherTestUtils.getUniqueJobParametersBuilder()
                .toJobParameters();
        //when
        JobExecution jobExecution = jobLauncherTestUtils.launchJob(jobParameters);

        //then
        Assertions.assertThat(jobExecution.getStatus()).isEqualTo(BatchStatus.COMPLETED);
        Assertions.assertThat(studentRepository.count()).isEqualTo(10);
    }

}
