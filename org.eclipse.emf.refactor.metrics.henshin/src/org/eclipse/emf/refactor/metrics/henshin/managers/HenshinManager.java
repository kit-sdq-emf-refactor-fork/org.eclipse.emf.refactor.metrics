package org.eclipse.emf.refactor.metrics.henshin.managers;

import java.util.List;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.henshin.interpreter.EGraph;
import org.eclipse.emf.henshin.interpreter.Engine;
import org.eclipse.emf.henshin.interpreter.Match;
import org.eclipse.emf.henshin.interpreter.RuleApplication;
import org.eclipse.emf.henshin.interpreter.impl.EGraphImpl;
import org.eclipse.emf.henshin.interpreter.impl.EngineImpl;
import org.eclipse.emf.henshin.interpreter.impl.MatchImpl;
import org.eclipse.emf.henshin.interpreter.impl.RuleApplicationImpl;
import org.eclipse.emf.henshin.interpreter.util.InterpreterUtil;
import org.eclipse.emf.henshin.model.Module;
import org.eclipse.emf.henshin.model.Parameter;
import org.eclipse.emf.henshin.model.Rule;

public class HenshinManager {

	private final static String MAIN_RULE = "mainRule";
	private final static String CONTEXT = "context";

	public static double run(String transformationPath, EObject context) {
		EObject root = EcoreUtil.getRootContainer(context);		
		EGraph graph = new EGraphImpl();
		graph.addTree(root);
		Engine engine = new EngineImpl();
		Module module = (Module) HenshinFileManager.loadFile(transformationPath);
		Rule rule = (Rule) module.getUnit(MAIN_RULE);
		Match prematch = new MatchImpl(rule);
		for (Parameter par : rule.getParameters()) {
			if (par.getName().equals(CONTEXT)) {
				prematch.setParameterValue(par, context);
			}
		}
		RuleApplication application = new RuleApplicationImpl(engine, graph, rule, prematch); 
		List<Match> matches = InterpreterUtil.findAllMatches(engine,
				application.getRule(), application.getEGraph(),
				application.getPartialMatch());
		return matches.size();
	}
}
