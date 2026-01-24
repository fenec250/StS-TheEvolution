package evolutionmod.cards;

import basemod.abstracts.CustomSavable;
import basemod.helpers.TooltipInfo;
import com.badlogic.gdx.graphics.Color;
import com.evacipated.cardcrawl.mod.stslib.cards.interfaces.StartupCard;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import evolutionmod.orbs.*;
import evolutionmod.patches.EvolutionEnum;

import java.util.List;

public class LoyalWarrior
		extends BaseEvoCard implements CustomSavable<Integer>, StartupCard, GlowingCard {
	public static final String ID = "evolutionmodV2:LoyalWarrior";
	public static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
	public static final String NAME = cardStrings.NAME;
	public static final String DESCRIPTION = cardStrings.DESCRIPTION;
	public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
	public static final String[] EXTENDED_DESCRIPTION = cardStrings.EXTENDED_DESCRIPTION;
	public static final String IMG_PATH = "evolutionmod/images/cards/LoyalWarrior.png";
	private static final int COST = 1;
	private static final int DAMAGE_AMT = 8;
	private static final int UPGRADE_DAMAGE_AMT = 4;

	private int geneIndex;
	private AbstractGene gene;

	public LoyalWarrior() {
		super(ID, NAME, IMG_PATH, COST, DESCRIPTION,
				CardType.ATTACK, EvolutionEnum.EVOLUTION_V2_BLUE,
				CardRarity.BASIC, CardTarget.ENEMY);
		this.damage = this.baseDamage = DAMAGE_AMT;
		this.magicNumber = this.baseMagicNumber = UPGRADE_DAMAGE_AMT;
		this.exhaust = true;
		this.geneIndex = -1;
		resetGene();
	}

	private LoyalWarrior(int geneIndex) {
		super(ID, NAME, IMG_PATH, COST, DESCRIPTION,
				CardType.ATTACK, EvolutionEnum.EVOLUTION_V2_BLUE,
				CardRarity.BASIC, CardTarget.SELF);
		this.damage = this.baseDamage = DAMAGE_AMT;
		this.magicNumber = this.baseMagicNumber = UPGRADE_DAMAGE_AMT;
		this.exhaust = true;
		this.geneIndex = geneIndex;
		resetGene();
	}

	@Override
	public boolean atBattleStartPreDraw() {
		if (this.upgraded) {
			addToBot(((AbstractGene)this.gene.makeCopy()).getChannelAction());
//			this.gene.getAdaptation().apply(AbstractDungeon.player, null);
		}
		return false;
	}

	@Override
	public void use(AbstractPlayer p, AbstractMonster m) {
		addToBot(new DamageAction(
				m, new DamageInfo(p, damage, this.damageTypeForTurn),
				AbstractGameAction.AttackEffect.BLUNT_HEAVY));
		if (!this.upgraded) {
			BaseEvoCard.formEffect(this.gene.ID);
		}
	}

	@Override
	public void applyPowers() {
		alterDamageAround(super::applyPowers);
	}

	@Override
	public void calculateCardDamage(AbstractMonster mo) {
		alterDamageAround(() -> super.calculateCardDamage(mo));
	}

	private void alterDamageAround(Runnable supercall) {
		this.baseDamage = DAMAGE_AMT + (upgraded ? UPGRADE_DAMAGE_AMT : 0);
		if (!this.upgraded && this.gene != null && BaseEvoCard.isPlayerInThisForm(this.gene.ID)) {
			this.baseDamage += this.magicNumber;
		}
		supercall.run();
		this.baseDamage = DAMAGE_AMT + (upgraded ? UPGRADE_DAMAGE_AMT : 0);
		this.isDamageModified = this.damage != this.baseDamage;
	}


	@Override
	public AbstractCard makeCopy() {
		return this.gene == null ? new LoyalWarrior() : new LoyalWarrior(this.geneIndex);
	}

	@Override
	public void upgrade() {
		if (!this.upgraded) {
			this.upgradeName();
			this.upgradeDamage(UPGRADE_DAMAGE_AMT);
			this.rawDescription = UPGRADE_DESCRIPTION;
			this.initializeDescription();
			this.resetGene();
		}
	}

	@Override
	public int getNumberOfGlows() {
		return upgraded ? 0 : 1;
	}

	@Override
	public boolean isGlowing(int glowIndex) {
		return isPlayerInThisForm(gene.ID);
	}

	@Override
	public Color getGlowColor(int glowIndex) {
		switch (gene.ID) {
			case HarpyGene2.ID: return HarpyGene2.COLOR.cpy();
			case MerfolkGene2.ID: return MerfolkGene2.COLOR.cpy();
			case LavafolkGene2.ID: return LavafolkGene2.COLOR.cpy();
			case CentaurGene2.ID: return CentaurGene2.COLOR.cpy();
			case LizardGene2.ID: return LizardGene2.COLOR.cpy();
			case BeastGene2.ID: return BeastGene2.COLOR.cpy();
			case PlantGene2.ID: return PlantGene2.COLOR.cpy();
			case ShadowGene2.ID: return ShadowGene2.COLOR.cpy();
			case LymeanGene2.ID: return LymeanGene2.COLOR.cpy();
			case InsectGene2.ID: return InsectGene2.COLOR.cpy();
			case SuccubusGene2.ID: return SuccubusGene2.COLOR.cpy();
			default: return AbstractCard.BLUE_BORDER_GLOW_COLOR.cpy();
		}
	}

	@Override
	public List<TooltipInfo> getCustomTooltips() {
		if (customTooltips == null) {
			super.getCustomTooltips();
			customTooltips.add(new TooltipInfo("Randomized form",
					"The form on this card is selected when the run starts and varies from one run to another."));
		}
		return  customTooltips;
	}

	private void resetGene() {
		if (this.geneIndex < 0) {
			if (!CardCrawlGame.isInARun() || AbstractDungeon.miscRng == null) {
				return;
			}
			this.geneIndex = AbstractDungeon.miscRng.random(11 - 1);
		}
		AbstractGene[] validGenes = {
				new PlantGene2(),
				new MerfolkGene2(),
				new HarpyGene2(),
				new LavafolkGene2(),
				new SuccubusGene2(),
				new LymeanGene2(),
				new InsectGene2(),
				new ShadowGene2(),
				new LizardGene2(),
				new CentaurGene2(),
				new BeastGene2()};
		this.gene = validGenes[this.geneIndex];
		this.rawDescription = EXTENDED_DESCRIPTION[0] + (!upgraded
				? this.gene.ID + EXTENDED_DESCRIPTION[1]
				: EXTENDED_DESCRIPTION[2] + this.gene.ID + EXTENDED_DESCRIPTION[3]);
		initializeDescription();
	}

	@Override
	public Integer onSave() {
		return this.geneIndex;
	}

	@Override
	public void onLoad(Integer integer) {
		this.geneIndex = integer;
		resetGene();
	}
}