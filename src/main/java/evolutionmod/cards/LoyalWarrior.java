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
import evolutionmod.orbs.AbstractGene;
import evolutionmod.orbs.BeastGene;
import evolutionmod.orbs.CentaurGene;
import evolutionmod.orbs.HarpyGene;
import evolutionmod.orbs.InsectGene;
import evolutionmod.orbs.LavafolkGene;
import evolutionmod.orbs.LizardGene;
import evolutionmod.orbs.LymeanGene;
import evolutionmod.orbs.MerfolkGene;
import evolutionmod.orbs.PlantGene;
import evolutionmod.orbs.ShadowGene;
import evolutionmod.orbs.SuccubusGene;
import evolutionmod.patches.AbstractCardEnum;

import java.util.List;

public class LoyalWarrior
		extends BaseEvoCard implements CustomSavable<Integer>, StartupCard, GlowingCard {
	public static final String ID = "evolutionmod:LoyalWarrior";
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
				CardType.ATTACK, AbstractCardEnum.EVOLUTION_BLUE,
				CardRarity.BASIC, CardTarget.ENEMY);
		this.damage = this.baseDamage = DAMAGE_AMT;
		this.magicNumber = this.baseMagicNumber = UPGRADE_DAMAGE_AMT;
		this.exhaust = true;
		this.geneIndex = -1;
		resetGene();
	}

	private LoyalWarrior(int geneIndex) {
		super(ID, NAME, IMG_PATH, COST, DESCRIPTION,
				CardType.ATTACK, AbstractCardEnum.EVOLUTION_BLUE,
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
			this.gene.getAdaptation().apply(AbstractDungeon.player, null);
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
			case HarpyGene.ID: return HarpyGene.COLOR.cpy();
			case MerfolkGene.ID: return MerfolkGene.COLOR.cpy();
			case LavafolkGene.ID: return LavafolkGene.COLOR.cpy();
			case CentaurGene.ID: return CentaurGene.COLOR.cpy();
			case LizardGene.ID: return LizardGene.COLOR.cpy();
			case BeastGene.ID: return BeastGene.COLOR.cpy();
			case PlantGene.ID: return PlantGene.COLOR.cpy();
			case ShadowGene.ID: return ShadowGene.COLOR.cpy();
			case LymeanGene.ID: return LymeanGene.COLOR.cpy();
			case InsectGene.ID: return InsectGene.COLOR.cpy();
			case SuccubusGene.ID: return SuccubusGene.COLOR.cpy();
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
			this.geneIndex = AbstractDungeon.miscRng.random(8 - 1);
		}
		AbstractGene[] validGenes = {
				new MerfolkGene(),
				new HarpyGene(),
				new LavafolkGene(),
				new SuccubusGene(),
				new LymeanGene(),
				new BeastGene(),
				new LizardGene(),
				new ShadowGene()};
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