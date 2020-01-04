package evolutionmod.cards;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.defect.ChannelAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.ConstrictedPower;
import com.megacrit.cardcrawl.powers.ThornsPower;
import evolutionmod.orbs.PlantGene;
import evolutionmod.patches.AbstractCardEnum;
import evolutionmod.powers.BramblesPower;

public class Constrict
        extends CustomCard {
    public static final String ID = "evolutionmod:Constrict";
    public static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    public static final String IMG_PATH = "evolutionmod/images/cards/strike.png";
    private static final int COST = 2;
    private static final int BLOCK_AMT = 8;
    private static final int UPGRADE_BLOCK_AMT = 4;

    public Constrict() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION,
                CardType.SKILL, AbstractCardEnum.EVOLUTION_BLUE,
                CardRarity.RARE, CardTarget.ENEMY);
        this.block = this.baseBlock = BLOCK_AMT;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        AbstractDungeon.actionManager.addToBottom(new GainBlockAction(p, p, this.block));
        int constricted = 0;
        if (p.hasPower(ThornsPower.POWER_ID)) {
            constricted += p.getPower(ThornsPower.POWER_ID).amount;
        }
        if (p.hasPower(BramblesPower.POWER_ID)) {
            constricted += p.getPower(BramblesPower.POWER_ID).amount;
        }
        if (constricted > 0) {
            final int finalConstricted = constricted;
            if (!this.upgraded) {
                AbstractDungeon.actionManager.addToBottom(
                        new ApplyPowerAction(m, p, new ConstrictedPower(m, p, finalConstricted), finalConstricted));
            } else {
                AbstractDungeon.getMonsters().monsters.forEach(mo ->
                        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(
                                mo, p, new ConstrictedPower(mo, p, finalConstricted), finalConstricted, true))
                );
            }
        }
        AbstractDungeon.actionManager.addToBottom(new ChannelAction(new PlantGene()));
    }

    @Override
    public AbstractCard makeCopy() {
        return new Constrict();
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeBlock(UPGRADE_BLOCK_AMT);
            this.rawDescription = UPGRADE_DESCRIPTION;
            initializeDescription();
            this.target = CardTarget.ALL_ENEMY;
        }
    }
}