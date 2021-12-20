#install.packages("ggvis")
#install.packages("dplyr")
#install.packages("gmodels") 

library(ggvis)
library(dplyr)
library(class) # KNN including
library(gmodels) #CrossTable including


setwd("C:/Users/duswo/Desktop/3학년 2학기/코딩과 인문사회-송태호/project") # 경로 설정
diabetes = read.csv("diabetes.csv") # 파일 불러오기
ls(diabetes) # diabetes에 있는 목록들 확인

summary(diabetes) 

# Scaling # 단위를 맞춰주는거
min_max_scaling <- function(x) {
  num <- x - min(x)
  denom <- max(x) - min(x)
  return (num/denom)
}

# 전체적인 요소를 고려한 모델, 선천적인 요소를 고려한 모델, 후천적인 요소를 고려한 모델을 만들기 위한 세팅.
scaled_diabetes_total <- as.data.frame(lapply(diabetes[1:8], min_max_scaling))
scaled_diabetes_innate <- as.data.frame(lapply(diabetes[c(2, 3, 5, 7, 8)], min_max_scaling)) # 데이터에서 2, 3, 5, 7, 8번이 각각 "Glucose(혈당 수치), BloodPressure(혈압), Insulin(인슐린 수치), DiabetesPedigreeFunction(당뇨 가족력), Age(나이)"이다. 
scaled_diabetes_acquired <- as.data.frame(lapply(diabetes[c(1,4,6)], min_max_scaling)) # 데이터에서 1, 4, 6번이 각각 "Pregnancies(임신), SkinThickness(삼두근 팔뚝살 두께, 피하지방), BMI(체질량지수)"이다.

sumvectors_total = c()
sumvectors_innate = c()
sumvectors_acquired = c()

# 시간이 3분 걸리기 때문에 알림 추가.
cat("It takes about 3 minutes.\n")

#summary(scaled_diabetes)
for(i in 1:100){ # 난수 발생을 새롭게 함으로써 새로운 데이터를 뽑기 위한 것
  # Random number generator initialization # 랜덤으로 샘플을 뽑기위한 설정
  set.seed(1234+i) # (1234+i)를 이용하여 새로운 난수 발생
  
  # Traing vs. Test Set : 70% vs. 30 # 샘플을 7 : 3으로 뽑기
  random_samples <- sample(2, nrow(diabetes), replace=TRUE, prob=c(0.7,0.3))
  # random_samples를 입력하여 확인할 수 있음.
  
  # Training Data Set # 1번(Training 할 것)으로 마킹된것을 다 뽑는다는거
  diabetes.training_total <- scaled_diabetes_total[random_samples == 1,]
  diabetes.training_innate <- scaled_diabetes_innate[random_samples == 1,]
  diabetes.training_acquired <- scaled_diabetes_acquired[random_samples == 1,]
  # Training Target # 9번째 목록인 Diabetes의 유무 추가하기
  diabetes.train_target_total <- diabetes[random_samples == 1, 9]
  diabetes.train_target_innate <- diabetes[random_samples == 1, 9]
  diabetes.train_target_acquired <- diabetes[random_samples == 1, 9]
  # Test Data Set (30%) # 2번(Test 할 것)으로 마킹된것을 다 뽑는다는거
  diabetes.test_total <- scaled_diabetes_total[random_samples == 2,]
  diabetes.test_innate <- scaled_diabetes_innate[random_samples == 2,]
  diabetes.test_acquired <- scaled_diabetes_acquired[random_samples == 2,]
  # Test Target (30%) # 9번째 목록인 Diabetes의 유무 추가하기
  diabetes.test_target_total <- diabetes[random_samples == 2, 9]
  diabetes.test_target_innate <- diabetes[random_samples == 2, 9]
  diabetes.test_target_acquired <- diabetes[random_samples == 2, 9]
  
  
  predictionByK_total = c() # 각각의 k에 해당하는 예측률을 저장하기 위한 vector
  predictionByK_innate = c()
  predictionByK_acquired = c()
  
  # KNN with k = x # 학습시키기
  for(t in 1:100){ # 반복문을 활용하여 k를 1부터 100까지 변화
    diabetes_model_total <- knn(train = diabetes.training_total, test = diabetes.test_total, 
                                cl = diabetes.train_target_total, k = t)
    diabetes_model_innate <- knn(train = diabetes.training_innate, test = diabetes.test_innate, 
                                 cl = diabetes.train_target_innate, k = t)
    diabetes_model_acquired <- knn(train = diabetes.training_acquired, test = diabetes.test_acquired, 
                                   cl = diabetes.train_target_acquired, k = t)
    # summary(iris_model)로 확인가능 // summary(iris.test_target)을 이용해서 두개 비교가능
    # 어떤게 잘 못 분류된건지 확인가능
    
    # Learning Result
    #summary(diabetes_model)
    #summary(diabetes.test_target)
    
    # Evaluation of Learning
    
    # 정분류율 : 전체 관측치 중 실제값과 예측치가 일치한 정도를 나타내는 것 # "예측 정확도"로 사용할 것임.
    tt_total = table(diabetes.test_target_total, diabetes_model_total)
    prediction_total = sum(tt_total[row(tt_total) == col(tt_total)]) / sum(tt_total)
    
    tt_innate = table(diabetes.test_target_innate, diabetes_model_innate)
    prediction_innate = sum(tt_innate[row(tt_innate) == col(tt_innate)]) / sum(tt_innate)
    
    tt_acquired = table(diabetes.test_target_acquired, diabetes_model_acquired)
    prediction_acquired = sum(tt_acquired[row(tt_acquired) == col(tt_acquired)]) / sum(tt_acquired)
    #print(prediction) # 정분류율
    #cat("\n")
    predictionByK_total = c(predictionByK_total, prediction_total) # k값과 일치하는 인덱스 번호에 값을 추가하기.
    predictionByK_innate = c(predictionByK_innate, prediction_innate)
    predictionByK_acquired = c(predictionByK_acquired, prediction_acquired)
  }
  
  if(i==1){ #predictionByK의 값들의 합인 sumvectors의 크기 설정을 위한 초기 설정.
    sumvectors_total = predictionByK_total
    sumvectors_innate = predictionByK_innate
    sumvectors_acquired = predictionByK_acquired
  }
  
  sumvectors_total = sumvectors_total + predictionByK_total # 난수에 의한 변화의 평균을 구하기 위한 predictionByK 값들을 합.
  sumvectors_innate = sumvectors_innate + predictionByK_innate
  sumvectors_acquired = sumvectors_acquired + predictionByK_acquired
}

cat("[Model using total elements]\n")
bestK_total = which.max(sumvectors_total) # 예측률이 가장 높은 k 값
cat("Best K : ", bestK_total, "\n")
cat("The highest prediction rate (%) : ", sumvectors_total[bestK_total], "%\n\n")


cat("[Model using innate elements : Glucose(혈당 수치), BloodPressure(혈압), Insulin(인슐린 수치), DiabetesPedigreeFunction(당뇨 가족력), Age(나이)]\n")
bestK_innate = which.max(sumvectors_innate) # 예측률이 가장 높은 k 값
cat("Best K : ", bestK_innate, "\n")
cat("The highest prediction rate(%) : ", sumvectors_innate[bestK_innate], "%\n\n")

cat("[Model using acquired elements : Pregnancies(임신), SkinThickness(삼두근 팔뚝살 두께, 피하지방), BMI(체질량지수)]\n")
bestK_acquired = which.max(sumvectors_acquired) # 예측률이 가장 높은 k 값
cat("Best K : ", bestK_acquired, "\n")
cat("The highest prediction rate(%) : ", sumvectors_acquired[bestK_acquired], "%\n\n")

# 그래프를 이용하여 값 확인
par(mfrow = c(1, 3))
plot(1:100, sumvectors_total, col = "black",  xlab = "k", ylab = "The rate of predcition (%)", main = "Model using total elements") # 예측률의 최고값은 '1'로 하고 '100'개의 샘플을 뽑았기 때문에, 이것이 확률.
plot(1:100, sumvectors_innate, col = "red",  xlab = "k", ylab = "The rate of predcition (%)", main = "Model using innate elements")
plot(1:100, sumvectors_acquired, col = "blue",  xlab = "k", ylab = "The rate of predcition (%)", main = "Model using acquired elements")


sum_innate = 0
sum_acquired = 0
for(i in -5:5){
  sum_innate = sum_innate + sumvectors_innate[bestK_innate + i]
  sum_acquired = sum_acquired + sumvectors_acquired[bestK_acquired + i]
}
cat("\n")
cat("High average prediction rate of a model using innate elements : ",sum_innate / 11, "%\n") # 가장 예측률이 높은 것을 기준으로 총 11개를 뽑았기 때문에 11개를 나누어주기.
cat("High average prediction rate of a model using acquired elements : ",sum_acquired / 11, "%\n")

if(sum_innate > sum_acquired){ # 선천적인 요소를 활용한 모델의 예측률이 후천적인 효소를 활용한 예측률보다 높을경우.
  cat("The prediction rate of models using innate elements is higher than that of models using acquired elements.")
} else if(sum_innate < sum_acquired){ # 후천적인 요소를 활용한 모델의 예측률이 선천적인 효소를 활용한 예측률보다 높을경우.
  cat("The prediction rate of models using acquired elements is higher than that of models using innate elements.")
} else { # 선척적인 요소와 후천적인 요소를 활용한 모델의 예측률이 같을 경우.
  cat("Models using innate elements and models using acquired elements have the same prediction rate.")
}

