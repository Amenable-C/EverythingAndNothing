#install.packages("ggvis")
#install.packages("dplyr")
#install.packages("gmodels") 

library(ggvis)
library(dplyr)
library(class) # KNN including
library(gmodels) #CrossTable including


setwd("C:/Users/duswo/Desktop/3학년 2학기/코딩과 인문사회-송태호/project")
diabetes = read.csv("diabetes.csv")
ls(diabetes)

summary(diabetes) 

#iris %>% 
  #ggvis(~Outcome, ~Age, fill = ~factor(Outcome)) %>%
  #layer_points()

# Scaling # 단위를 맞춰주는거
min_max_scaling <- function(x) {
  num <- x - min(x)
  denom <- max(x) - min(x)
  return (num/denom)
}

scaled_diabetes <- as.data.frame(lapply(diabetes[1:8], min_max_scaling))

summary(scaled_diabetes)

# Random number generator initialization # 이거 해줘야지 랜덤으로 됨
set.seed(1234)

# Traing vs. Test Set : 70% vs. 30 # 샘플 두개를 뽑는데, 0.7, 0.3으로 뽑는다는거
random_samples <- sample(2, nrow(diabetes), replace=TRUE, prob=c(0.7,0.3))
# random_samples를 입력하여 확인할 수 있음.

# Training Data Set # 1번으로 마킹된것을 다 뽑는다는거
diabetes.training <- scaled_diabetes[random_samples == 1,]
# Training Target # 5번은 스케일링 안 함. 그래서 그냥 가져오기
diabetes.train_target <- diabetes[random_samples == 1, 9]
# Test Data Set (30%)
diabetes.test <- scaled_diabetes[random_samples == 2,]
# Test Target (30%)
diabetes.test_target <- diabetes[random_samples == 2, 9]


# KNN with k = 3 #이렇게 학습시킨다는거
diabetes_model <- knn(train = diabetes.training, test = diabetes.test, 
                  cl = diabetes.train_target, k =10)
# summary(iris_model)로 확인가능 // summary(iris.test_target)을 이용해서 두개 비교가능
# 어떤게 잘 못 분류된건지 확인가능

# Learning Result
summary(diabetes_model)
summary(diabetes.test_target)

# Evaluation of Learning
CrossTable(x = diabetes.test_target, y = diabetes_model, prop.chisq = FALSE)
