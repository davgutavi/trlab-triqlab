package labutils;

import general.GST;
import general.GSTutils;
import general.Tricluster;

import java.util.Set;

public class Matching {
	
	public static double matching1(Tricluster tricluster, Tricluster target) {

		double r = 0.0;

		Set<Integer> gtri = GSTutils.getComponentSet(tricluster.getGenes());
		Set<Integer> ctri = GSTutils.getComponentSet(tricluster.getSamples());
		Set<Integer> ttri = GSTutils.getComponentSet(tricluster.getTimes());

		Set<Integer> gtar = GSTutils.getComponentSet(target.getGenes());
		Set<Integer> ctar = GSTutils.getComponentSet(target.getSamples());
		Set<Integer> ttar = GSTutils.getComponentSet(target.getTimes());

		double rg = GSTutils.componentIntersectionCount(gtri, gtar);
		double rc = GSTutils.componentIntersectionCount(ctri, ctar);
		double rt = GSTutils.componentIntersectionCount(ttri, ttar);

		double tgs = gtar.size();
		double tcs = ctar.size();
		double tts = ttar.size();

		double num = rg + rc + rt;

		double den = tgs + tcs + tts;

		r = num / den;

		return r;

	}
		

	public static double matching2(Tricluster tricluster, Tricluster target) {

		double r = 0.0;

		Set<GST> ctricluster = GSTutils.getCells(tricluster);

		Set<GST> ctarget = GSTutils.getCells(target);

		double repeated = GSTutils.cellIntersectionCount(ctricluster, ctarget);

		double total = GSTutils.cellUnionCount(ctricluster, ctarget);

		r = repeated / total;

		return r;

	}

	public static double matching3(Tricluster tricluster, Tricluster target) {

		double r = 0.0;

		Set<GST> ctricluster = GSTutils.getCells(tricluster);

		Set<GST> ctarget = GSTutils.getCells(target);

		double repeated = GSTutils.cellIntersectionCount(ctricluster, ctarget);

		double total = ctarget.size();

		r = repeated / total;

		// System.out.println(repeated+" / "+total+" = "+r);

		return r;

	}

	public static double matching4(Tricluster tricluster, Tricluster target) {

		double r = 0.0;

		Set<Integer> gtri = GSTutils.getComponentSet(tricluster.getGenes());
		Set<Integer> ctri = GSTutils.getComponentSet(tricluster.getSamples());
		Set<Integer> ttri = GSTutils.getComponentSet(tricluster.getTimes());

		Set<Integer> gtar = GSTutils.getComponentSet(target.getGenes());
		Set<Integer> ctar = GSTutils.getComponentSet(target.getSamples());
		Set<Integer> ttar = GSTutils.getComponentSet(target.getTimes());

		double rg = GSTutils.componentIntersectionCount(gtri, gtar);
		double rc = GSTutils.componentIntersectionCount(ctri, ctar);
		double rt = GSTutils.componentIntersectionCount(ttri, ttar);

		double ug = GSTutils.componentUnionCount(gtri, gtar);
		double uc = GSTutils.componentUnionCount(ctri, ctar);
		double ut = GSTutils.componentUnionCount(ttri, ttar);

		double num = rg + rc + rt;

		double den = ug + uc + ut;

		r = num / den;

		return r;

	}

}
