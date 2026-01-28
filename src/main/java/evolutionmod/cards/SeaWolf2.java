package evolutionmod.cards;

import com.evacipated.cardcrawl.mod.stslib.powers.abstracts.TwoAmountPower;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import evolutionmod.orbs.MerfolkGene2;
import evolutionmod.patches.EvolutionEnum;
import evolutionmod.powers.HumanFormPower;
import evolutionmod.powers.MasteryPower;
import evolutionmod.relics.PowerFocus;

public class SeaWolf2
        extends BaseEvoCard {
    public static final String ID = "evolutionmodV2:SeaWolf";
    public static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    public static final String IMG_PATH = "evolutionmod/images/cards/SeaWolf.png";
    private static final int COST = 1;
    private static final int DAMAGE_AMT = 4;
    private static final int DAMAGE_PER_3_AMT = 1;
    private static final int UPGRADE_DAMAGE_PER_3_AMT = 1;

    public SeaWolf2() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION,
                CardType.ATTACK, EvolutionEnum.EVOLUTION_V2_BLUE,
                CardRarity.UNCOMMON, CardTarget.ENEMY);
        this.damage = this.baseDamage = DAMAGE_AMT;
        this.magicNumber = this.baseMagicNumber = DAMAGE_PER_3_AMT;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new DamageAction(
						m, new DamageInfo(p, damage, damageTypeForTurn),
						AbstractGameAction.AttackEffect.SLASH_VERTICAL));
        addToBot(new MerfolkGene2().getChannelAction());
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
//        AbstractPlayer player = AbstractDungeon.player;
//        boolean human = player.hasPower(HumanFormPower.POWER_ID);
//        int triggers = player.orbs.size() < 1 ? 0
//                : (1
//                    + (player.hasPower(MasteryPower.powerIdForGene(MerfolkGene2.ID)) ? player.getPower(MasteryPower.powerIdForGene(MerfolkGene2.ID)).amount : 0)
//                    + (human ? player.getPower(HumanFormPower.POWER_ID).amount : 0)
//                + ((player.hasPower(Absorption.AbsorptionPower.POWER_ID) && ((TwoAmountPower) player.getPower(Absorption.AbsorptionPower.POWER_ID)).amount2 > 0)?1:0)
////                    + (player.hasRelic(PowerFocus.ID) && !player.getRelic(PowerFocus.ID).usedUp)
//                    * (human?1:0)
//        );
		this.baseDamage = DAMAGE_AMT + ((AbstractDungeon.player.currentBlock) / 3) * this.magicNumber;
		supercall.run();
		this.baseDamage = DAMAGE_AMT;
		this.isDamageModified = this.damage != this.baseDamage;
	}

    @Override
    public AbstractCard makeCopy() {
        return new SeaWolf2();
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeMagicNumber(UPGRADE_DAMAGE_PER_3_AMT);
			initializeDescription();
        }
    }
}