<%@ include file="/WEB-INF/jsp/taglibs.jsp" %>

<s:layout-render name="/WEB-INF/jsp/depot/layout.jsp">
	<s:layout-component name="pageTitle">Kinek</s:layout-component>
	
  	<s:layout-component name="body">
  	
  	<style type="text/css">
	  	.headshot
	  	{
	  		float: left;
	  		padding: 0px 5px 0px 0px;
	  	}
	 </style>
  	
	<!-- CONTENTWRAPPER -->
	<div id="contentWrapper">
	
		<!-- CONTENT -->
		<div id="content" class="clearfix">
			
			<h1>About Us</h1>
			
			<!-- ABOUT US -->
			<s:form beanclass="org.webdev.kpoint.action.AboutActionBean">
				<div style="display: inline-block; width: 60%; float: left;">
					<p>
						Kinek has developed a nationwide network of business locations that 
						provide alternative delivery locations for e-commerce shoppers.  By 
						directing their deliveries to a KinekPoint location, consumers need 
						not worry about doorstop theft or missing deliveries and retrieving 
						packages from courier outlets.
					</p>
					<p>
						Using the Kinek website, a consumer selects a delivery location called 
						a KinekPoint from a number of community-based businesses such as mail 
						and parcel stores. When their package arrives at the KinekPoint partner 
						location, the user receives an email and text message notification 
						alerting them that their delivery is ready to be picked up at their 
						convenience. In addition to reducing risk and added convenience, many 
						consumers are pleased to know that they are able to reduce their carbon 
						footprint by shopping on-line and reducing the "last mile" delivery truck 
						rolls.							
					</p>
					<p>
						Kinek is creating an ecosystem by linking merchants, delivery firms, receiving 
						locations and consumers in providing a package delivery system that is 
						streamlined, efficient, convenient, and secure.
					</p>	
					<br/>
					<h2 style="font-size: 20px">Management Team</h2>
					<p>
						Kinek's management team consists of a dynamic group of professionals with 
						significant experience in start-up organizations and large multi-national 
						corporations.
					</p>	
					<div>		
						<img class="headshot" src="resource/images/about_kerry.jpg"></img> 		
						<p>							
							<strong>Kerry McLellan, Ph.D., Chief Executive Officer</strong>, prior 
							to establishing Kinek, was the Chief Operating Officer of 724 
							Solutions, Inc., where he helped grow the company from less than 20 
							employees to over 500. The IPO of 724 Solutions Inc. was one of the most
							successful in Canadian history. In addition, Kerry has several patents 
							pending on e-commerce technology and previously designed the back office 
							consolidation strategy for three of Canada's financial institutions. 
							Kerry holds a Ph.D. in Business Strategy from University of Western 
							Ontario's Richard Ivey School of Business and has served on the faculty 
							of Business Administration at the University of New Brunswick - Saint 
							John, New Brunswick, Canada.
						</p>	
					</div>
					<div>		
						<img class="headshot" src="resource/images/about_barb.jpg"></img> 
						<p>
							<strong>Barbara Marcolin, Research Director</strong>, is responsible for Kinek's research and 
							development plans. Technical software research for web-based component development products 
							is conducted in a standardized Research Process, gathering empirical results. 
							In addition, Barb oversees trending research for a variety of new product 
							developments. Prior to Kinek, Barb was an Associate Professor of Management 
							Information Systems, Haskayne School of Business, University of Calgary. Barb 
							has also worked in an advisory capacity with many technology firms.  Barb earned 
							a Bachelor of Commerce degree from the University of Calgary and a Doctorate 
							of Philosophy in Business Administration degree from the IVEY School of Business, 
							University of Western Ontario, Canada; both degrees are in Management Information Systems.
						</p>	
					</div>	
					<div>	
						<img class="headshot" src="resource/images/about_brad.jpg"></img> 		
						<p>							
							<strong>Brad Rideout, Product Development</strong>, is responsible for the design, 
							architecture, testing, and deployment of the software components for Kinek solutions. 
							This role also includes responsibility for guidance in the implementation of a 
							"Culture of Quality" for product development. Prior to Kinek, Brad was Development 
							Manager at Clearpicture Corporation where he was responsible for managing and leading 
							a team of analysts, architects, developers, and testers. During his tenure with smartForce 
							(now Skillsoft) he served as the company's Chief Architect. Brad's entrepreneur endeavors 
							include co-founding PenguinFire Media in  2003 and InfoDOG 1996, which he lead to 
							profitability prior to its sale. Brad earned a Bachelor of Science - Computer Science 
							degree from University of New Brunswick, New Brunswick, Canada.
						</p>
					</div>
					<div>
						<img class="headshot" src="resource/images/about_kendall.jpg"></img> 
						<p>							
							<strong>Kendall McMenamon, Manager of Product Management</strong>, is responsible for all 
							facets of product management, indentifying and prioritizing marketing requirements according 
							to business goals and user needs. He has a rich history in the management of communication 
							and information technology and a proven entrepreneurial background. In 2003 he co-founded 
							PenguinFire Media an interactive media company and prior to that he served as the President 
							of AnyWare Group, an international wireless consulting company that he co-founded. Kendall 
							is founding partner of AdvancePublishing Corp. and has held board positions with a variety 
							of companies.  He earned a Bachelor of Business Administration degree from University of New 
							Brunswick - Saint John. New Brunswick, Canada.
						</p>	
					</div>				
				</div>
				
				<div style="display: inline-block; width: 35%; float: right;">
					<img width="300" height="180" src="resource/images/aboutus.jpg"></img> 
					<p>
						Kinek creates relationships with existing businesses to receive their 
						parcels with improved safety, security, and convenience. We are always 
						looking for world class people who want to make a difference! Send your 
						resume to: <a href="mailto:HR2010@Kinek.com">HR2010@Kinek.com</a>
					</p>
				</div>
			</s:form>
			<!-- /ABOUT US -->
					
		</div>
		<!-- /CONTENT -->
	
	</div>
	<!-- /CONTENTWRAPPER -->

  </s:layout-component>
</s:layout-render>
	
	