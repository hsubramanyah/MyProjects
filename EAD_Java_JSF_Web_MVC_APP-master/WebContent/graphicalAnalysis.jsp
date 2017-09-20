<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="f" uri="http://java.sun.com/jsf/core"%>
<%@ taglib prefix="h" uri="http://java.sun.com/jsf/html"%>
<%@ taglib prefix="t" uri="http://myfaces.apache.org/tomahawk"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>f16g321 Graphical Analysis</title>
</head>
<body>
	<f:view>
		<h3 align="center">f16g321 Graphical Analysis</h3>
		<hr />
		<br />

		<div align="center">

			<h:form>
				<h:commandButton type="submit" value="Change Role"
					action="#{dBAccessActionBean.back}" /> &nbsp;&nbsp;&nbsp;
				<h:commandButton type="submit" value="Instructor Home Page"
					action="#{dBAccessActionBean.next}" /> &nbsp;&nbsp;&nbsp;
				<h:commandButton type="submit" value="Logout"
					action="#{dBAccessActionBean.logout}" />


			</h:form>
			<br /> <br />
		</div>
		<hr />
		<br />
		<div align="left">
			<h:form>
				<h:outputText value="#{messageBean.errorMessage}" style="color:red"
					rendered="#{messageBean.renderErrorMessage}" />
				<h:outputText value="#{messageBean.successMessage}"
					style="color:green" rendered="#{messageBean.renderSuccessMessage}" />
				<h:panelGrid columns="6">
					<h:commandButton type="submit" value="List Courses"
						action="#{instructorActionBean.listCourse}" />
					<h:commandButton type="submit" value="List Scores for Analysis"
						action="#{graphicalAnalysis.listScoresDataforAnalysis}" />
					<h:commandButton type="submit" value="List Graphs"
						action="#{graphicalAnalysis.listGraphs}" />
						<h:commandButton type="submit" value="Generate Numerical Analysis"
						action="#{graphicalAnalysis.numericalAnalysis}" />
						<h:commandButton type="submit" value="Generate Regression Analysis"
						action="#{graphicalAnalysis.regressionAnalysis}" />
					<h:commandButton type="submit" value="Generate Graph"
						action="#{graphicalAnalysis.generateGraph}" />
				</h:panelGrid>
				<h:panelGrid columns="4">
					<h:outputText value="Course List       " />
					<h:outputText value="Test/Scores List"
						rendered="#{graphicalAnalysis.renderXScoreList}" />
					<h:outputText value=""
						rendered="#{graphicalAnalysis.renderYScoreList}" />
					<h:outputText value="Graph List "
						rendered="#{graphicalAnalysis.renderGraphList}" />


					<h:selectOneListbox size="10" styleClass="selectOneListbox_mono"
						value="#{instructorActionBean.courseSelected}"
						rendered="#{instructorActionBean.courseListRendered}">
						<f:selectItems value="#{instructorActionBean.courseList}" />
					</h:selectOneListbox>

					<h:selectOneListbox size="10" styleClass="selectOneListbox_mono"
						value="#{graphicalAnalysis.scoreXSelected}"
						rendered="#{graphicalAnalysis.renderXScoreList}">
						<f:selectItems value="#{graphicalAnalysis.scoreList}" />
					</h:selectOneListbox>

					<h:selectOneListbox size="10" styleClass="selectOneListbox_mono"
						value="#{graphicalAnalysis.scoreYSelected}"
						rendered="#{graphicalAnalysis.renderYScoreList}">
						<f:selectItems value="#{graphicalAnalysis.scoreList}" />
					</h:selectOneListbox>

					<h:selectOneListbox size="10" styleClass="selectOneListbox_mono"
						value="#{graphicalAnalysis.graphTypeSelected}"
						rendered="#{graphicalAnalysis.renderGraphList}">
						<f:selectItems value="#{graphicalAnalysis.listGraph}" />
					</h:selectOneListbox>

					<h:outputText value="" />
					<h:outputText value="X(Predictor) Values"
						rendered="#{graphicalAnalysis.renderXScoreList}" />
					<h:outputText value="Y(Response) Values"
						rendered="#{graphicalAnalysis.renderYScoreList}" />
					<h:outputText value=""
						rendered="#{graphicalAnalysis.renderGraphList}" />

				</h:panelGrid>
				<br />
				<hr />
				<br />
			</h:form>
		</div>
		<div>
			<h:form rendered="#{graphicalAnalysis.renderNumAnalysis}">
				<h:panelGrid columns="2">
					<h:outputText value="Mimimum Score:" />
					<h:outputText value="#{graphicalAnalysis.minValue}" />
					<h:outputText value="Maximum Score:" />
					<h:outputText value="#{graphicalAnalysis.maxValue}" />
					<h:outputText value="Mean:" />
					<h:outputText value="#{graphicalAnalysis.mean}" />
					<h:outputText value="Variance:" />
					<h:outputText value="#{graphicalAnalysis.variance}" />
					<h:outputText value="Standard Deviation:" />
					<h:outputText value="#{graphicalAnalysis.std}" />
					<h:outputText value="Median:" />
					<h:outputText value="#{graphicalAnalysis.median}" />
					<h:outputText value="First Quartile:" />
					<h:outputText value="#{graphicalAnalysis.q1}" />
					<h:outputText value="Third Quartile:" />
					<h:outputText value="#{graphicalAnalysis.q3}" />
					<h:outputText value="Inter Quartile Range:" />
					<h:outputText value="#{graphicalAnalysis.iqr}" />
					<h:outputText value="Range:" />
					<h:outputText value="#{graphicalAnalysis.range}" />
				</h:panelGrid>
			</h:form>
			
			<h:form rendered="#{graphicalAnalysis.renderRegAnalysis}">
				<h:panelGrid columns="3" border="1">
					<h:outputText value="" />
					<h:outputText value="Co-efficient" />
					<h:outputText value="Co-efficient Standard Error" />
					<h:outputText value="Constant" />
					<h:outputText value="#{graphicalAnalysis.intercept}" />
					<h:outputText value="#{graphicalAnalysis.interceptStdError}" />
					<h:outputText value="#{graphicalAnalysis.scoreYSelected}" />
					<h:outputText value="#{graphicalAnalysis.slope}" />
					<h:outputText value="#{graphicalAnalysis.slopeStdError}" />
				</h:panelGrid>
				<br />
				<h:panelGrid columns="2" border="1">
				<h:outputText value="Significance:" />
				<h:outputText value="#{graphicalAnalysis.rSignificance}" /> 
				<h:outputText value="Regression Equation:" />
				<h:outputText value="#{graphicalAnalysis.rEquation}" />
				
				</h:panelGrid>
			</h:form>
		</div>
		<h:form>
			<div
				style="background-attachment: scroll; overflow: auto; height: auto; background-repeat: repeat"
				align="center">
				<h:graphicImage value="#{graphicalAnalysis.piechartPath}"
					height="450" width="600"
					rendered="#{graphicalAnalysis.renderPieChart }" alt="PieChart" />

			</div>
			<div
				style="background-attachment: scroll; overflow: auto; height: aoto; background-repeat: repeat"
				align="center">
				<h:graphicImage value="#{graphicalAnalysis.barchartPath}"
					height="450" width="600"
					rendered="#{graphicalAnalysis.renderBarChart}" alt="BarGraph" />

			</div>
			<div
				style="background-attachment: scroll; overflow: auto; height: aoto; background-repeat: repeat"
				align="center">
				<h:graphicImage value="#{graphicalAnalysis.histchartPath}"
					height="450" width="600"
					rendered="#{graphicalAnalysis.renderHistChart}" alt="Histogram" />

			</div>
			<div
				style="background-attachment: scroll; overflow: auto; height: aoto; background-repeat: repeat"
				align="center">
				<h:graphicImage value="#{graphicalAnalysis.xYchartPath}"
					height="450" width="600" 
					rendered="#{graphicalAnalysis.renderXYChart}" alt="XY Series" />

			</div>

		</h:form>

	</f:view>
</body>
</html>