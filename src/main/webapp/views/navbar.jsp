		<style>
			header {
				position: fixed;
				top: 0;
				align-self: start;
			    width: 100%;
			    box-shadow: 0 0.5rem 1.25rem rgba(0, 0, 0, 0.15);
			}
			ul {
				list-style-type: none;
				margin: 0;
				padding: 0;
				overflow: hidden;
				background-color: var(--c-primary);
				color: var(--c-white);
			}
			
			.nav-link {
				float: left;
			}
			
			.nav-link a {
			    display: block;
			    color: white;
			    text-align: center;
			    padding: 0.85rem 1rem;
			    text-decoration: none;
			}
			
			.nav-link a:hover {
			    background-color: #111;
			}
			
			.active {
				background-color: var(--c-primary-dark);
				color: white;
			}
		</style>
		
		<header>
			<nav>
				<ul>
				  <!--<li class="nav-link"><a href="index">Home</a></li>-->
				  <li class="nav-link" id="reg-link"><a href="${pageContext.request.contextPath}">Register</a></li>
				  <li class="nav-link" id="view-users-link"><a href="view-users">View Users</a></li>
				  <!-- <li class="nav-link"><a href="#">About</a></li> -->
				</ul>
			</nav>
		</header>
